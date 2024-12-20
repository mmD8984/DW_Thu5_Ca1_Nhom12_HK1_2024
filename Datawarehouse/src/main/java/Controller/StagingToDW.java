package Controller;

import DAO.DBProperties;
import DAO.JDBCUtil;
import Model.EmailService;

import java.sql.*;
import java.time.LocalDateTime;

public class LoadFileCSV {
    private static final String DB_URL = DBProperties.host;
    private static final String DB_USER = DBProperties.username;
    private static final String DB_PASSWORD = DBProperties.password;
    private EmailService emailService = new EmailService();

    public static void main(String[] args) {
        LoadFileCSV loader = new LoadFileCSV();
        loader.executeLoadProcess();
    }

    public void executeLoadProcess() {
        LocalDateTime currentTime = LocalDateTime.now();
        String tableStaging = "product_staging";
        int configId = getConfigId();  // Get the correct configId dynamically
        String filePath = "D:/DataWarehouse/DW_Thu5_Ca1_Nhom12_HK1_2024/Datawarehouse/phone_data.csv";

        if (configId == -1) {
            System.out.println("Error: No valid config found.");
            return; // Exit if configId is not valid
        }

        try (Connection controlConnection = JDBCUtil.getConnection()) {
            System.out.println("Checking if any process is running...");

            // Call stored procedure to check running process
            try (CallableStatement cstmt = controlConnection.prepareCall("{CALL control.check_running_process(?)}")) {
                cstmt.setInt(1, configId);  // Passing configId dynamically
                ResultSet rsRunningProcess = cstmt.executeQuery();
                if (rsRunningProcess.next()) {
                    int isProcessing = rsRunningProcess.getInt("is_processing");
                    String status = rsRunningProcess.getString("status");
                    if (isProcessing == 1 && !"START_EXTRACT".equals(status)) {
                        insertLog(controlConnection, configId, null, "Tiến trình đang chạy. Không thể tải dữ liệu.",
                                "FAILURE_RUNNING_PROCESS", currentTime);
                        sendEmail("Tiến trình đang chạy", "Hiện tại có tiến trình khác đang chạy. Không thể tải dữ liệu.",
                                "Tiến trình đang chạy", null);
                        System.out.println("Process is already running. Cannot proceed1.");
                        System.out.println("Process is already running. Cannot proceed2.");
                        System.out.println("Process is already running. Cannot proceed3.");
                        System.out.println("Process is already running. Cannot proceed4.");
                        return;
                    }
                }
            }

            // Call stored procedure to insert config if table is empty
            try (CallableStatement cstmt = controlConnection.prepareCall("{CALL control.insert_config()}")) {
                cstmt.execute();
                System.out.println("Configuration data inserted.");
            }

            System.out.println("Checking if table " + tableStaging + " exists...");
            // Check if table exists
            try (CallableStatement cstmt = controlConnection.prepareCall("{CALL check_table_exists(?)}")) {
                cstmt.setString(1, tableStaging);
                ResultSet rsTableCheck = cstmt.executeQuery();
                if (rsTableCheck.next() && rsTableCheck.getInt(1) == 0) {
                    // Call stored procedure to create table if not exists
                    try (CallableStatement createTableStmt = controlConnection.prepareCall("{CALL staging.create_product_staging_table()}")) {
                        createTableStmt.execute();
                    }
                    System.out.println("Table " + tableStaging + " has been created.");
                } else {
                    System.out.println("Table " + tableStaging + " already exists.");
                }
            }

            // Check if file exists
            if (filePath == null || !new java.io.File(filePath).exists()) {
                insertLog(controlConnection, configId, filePath, "Đường dẫn tệp dữ liệu không hợp lệ hoặc tệp không tồn tại: " + filePath,
                        "FAILURE_INVALID_FILE_PATH", currentTime);
                sendEmail("Lỗi đường dẫn tệp dữ liệu", "The system cannot find the file specified: " + filePath,
                        "Lỗi đường dẫn tệp dữ liệu", filePath);
                System.out.println("File does not exist at path: " + filePath);
                return;
            }

            // Truncate data in staging table
            try (CallableStatement cstmt = controlConnection.prepareCall("{CALL staging.truncate_staging_table(?)}")) {
                cstmt.setString(1, tableStaging);
                cstmt.execute();
                System.out.println("Old data in table " + tableStaging + " has been deleted.");
            }

            // Log status and start extracting data
            insertLog(controlConnection, configId, null, "Bắt đầu quá trình extract dữ liệu vào bảng " + tableStaging,
                    "START_EXTRACT", currentTime);

            // Run LOAD DATA SQL directly
            String fieldsTerminated = ",";  // Field separator for CSV file
            String optionallyEnclosed = "\"";  // Enclosure for values (if any)
            String linesTerminated = "\n";  // Line separator
            int ignoreRow = 1;  // Skip the header row

            String loadSql = "LOAD DATA INFILE '" + filePath + "' " +
                    "INTO TABLE " + tableStaging + " " +
                    "FIELDS TERMINATED BY '" + fieldsTerminated + "' " +
                    "OPTIONALLY ENCLOSED BY '" + optionallyEnclosed + "' " +
                    "LINES TERMINATED BY '" + linesTerminated + "' " +
                    "IGNORE " + ignoreRow + " ROWS " +
                    "(product_name, cpu, ram, storage_capacity, screen_size, price, battery_capacity, os, manufacturer)";

            // Execute the LOAD DATA command
            try (Statement stmt = controlConnection.createStatement()) {
                stmt.execute(loadSql);
                System.out.println("Data successfully loaded into table " + tableStaging);

                // Log success
                insertLog(controlConnection, configId, filePath, "Load data vào bảng " + tableStaging + " thành công",
                        "SUCCESS_LOAD_DATA", currentTime);

                sendEmail("Load data thành công", "Dữ liệu đã được tải thành công vào bảng " + tableStaging,
                        "Load data thành công", filePath);
            }

            // Update the processing status to finished
            try (CallableStatement cstmt = controlConnection.prepareCall("{CALL control.update_processing_status(?, ?)}")) {
                cstmt.setInt(1, configId);  // Using integer for configId
                cstmt.setInt(2, 0);  // Mark as finished
                cstmt.execute();
                System.out.println("Data load process finished. Status updated to 'finished'.");
            }

        } catch (Exception e) {
            try (Connection controlConnection = JDBCUtil.getConnection()) {
                insertLog(controlConnection, configId, filePath, "Error during data load process: " + e.getMessage(),
                        "FAILURE", currentTime);
                sendEmail("Lỗi trong quá trình tải dữ liệu", "Có lỗi xảy ra trong quá trình tải dữ liệu: " + e.getMessage(),
                        "Lỗi tải dữ liệu", filePath);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    // Get the correct configId dynamically from the database
    private int getConfigId() {
        int configId = 0;
        String query = "SELECT id_config FROM control.config_table WHERE is_processing = 0 ORDER BY id_config DESC LIMIT 1"; // Ensure we get the most recent valid config
        try (Connection connection = JDBCUtil.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                configId = rs.getInt("id_config");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return configId;
    }

    private void insertLog(Connection conn, int configId, String filePath, String message, String status,
                           LocalDateTime timestamp) throws SQLException {
        try (CallableStatement cstmt = conn.prepareCall("{CALL control.insert_log(?, ?, ?, ?, ?)}")) {
            cstmt.setInt(1, configId);  // Use dynamic configId here
            cstmt.setString(2, filePath);
            cstmt.setString(3, message);
            cstmt.setString(4, status);
            cstmt.setTimestamp(5, Timestamp.valueOf(timestamp));
            cstmt.execute();
        }
    }

    private void sendEmail(String subject, String body, String title, String filePath) {
        emailService.send("phamtrungtinpy363@gmail.com", subject, body);
        System.out.println("Email sent with subject: " + subject);
    }
}
