package Controller;

import DAO.DBProperties;
import DAO.JDBCUtil;
import Model.EmailService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadFileCSV {
    private static final String DB_URL = DBProperties.host;  // Sử dụng thông tin kết nối từ DBProperties
    private static final String DB_USER = DBProperties.username;
    private static final String DB_PASSWORD = DBProperties.password;

    private EmailService emailService = new EmailService();

    public static void main(String[] args) {
        LoadFileCSV loader = new LoadFileCSV();
        loader.loadCSVToStaging();
    }

    public void loadCSVToStaging() {
        LocalDateTime currentTime = LocalDateTime.now();
        String filePath = "D:/DataWarehouse/DW_Thu5_Ca1_Nhom12_HK1_2024/Datawarehouse/phone_data.csv";  // Đường dẫn file CSV
        String tableStaging = "product_staging";  // Tên bảng staging cần kiểm tra và tạo

        try (Connection connection = JDBCUtil.getConnection(); // Sử dụng JDBCUtil để lấy kết nối
             Statement stmt = connection.createStatement()) {

            // Kiểm tra nếu bảng product_staging đã tồn tại chưa
            String checkTableExists = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = '" + tableStaging + "'";
            ResultSet rs = stmt.executeQuery(checkTableExists);

            // Nếu bảng không tồn tại, tạo bảng
            if (rs.next() && rs.getInt(1) == 0) {
                // Tạo bảng product_staging nếu chưa tồn tại
                String createTableSQL = "CREATE TABLE IF NOT EXISTS product_staging (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT, " +
                        "product_name VARCHAR(255) NOT NULL, " +
                        "cpu VARCHAR(255), " +
                        "ram VARCHAR(255), " +
                        "storage_capacity VARCHAR(255), " +
                        "screen_size DECIMAL(5, 2), " +
                        "price DECIMAL(10, 2), " +
                        "battery_capacity VARCHAR(255), " +
                        "os VARCHAR(255), " +
                        "manufacturer VARCHAR(255)" +
                        ")";
                stmt.executeUpdate(createTableSQL);
                System.out.println("Bảng " + tableStaging + " đã được tạo.");
            } else {
                System.out.println("Bảng " + tableStaging + " đã tồn tại.");
            }

            // Lệnh LOAD DATA để tải dữ liệu vào bảng product_staging
            String fieldsTerminated = ",";  // Dấu phân cách các trường trong file CSV
            String optionallyEnclosed = "\"";  // Ký tự bao quanh các giá trị (nếu có)
            String linesTerminated = "\n";  // Ký tự kết thúc dòng
            int ignoreRow = 1;  // Bỏ qua dòng tiêu đề

            String loadSql = "LOAD DATA INFILE '" + filePath + "' " +
                    "INTO TABLE " + tableStaging + " " +
                    "FIELDS TERMINATED BY '" + fieldsTerminated + "' " +
                    "OPTIONALLY ENCLOSED BY '" + optionallyEnclosed + "' " +
                    "LINES TERMINATED BY '" + linesTerminated + "' " +
                    "IGNORE " + ignoreRow + " ROWS " +
                    "(product_name, cpu, ram, storage_capacity, screen_size, price, battery_capacity, os, manufacturer)";

            // Thực thi lệnh LOAD DATA
            stmt.execute(loadSql);

            // Gửi email thông báo thành công
            sendEmail("Load data vào bảng " + tableStaging + " thành công",
                    "Dữ liệu đã được tải thành công vào bảng " + tableStaging,
                    "Load data thành công", filePath);

            // Ghi log thành công
            insertLog("config_id_example", filePath, "Load data vào bảng " + tableStaging + " thành công",
                    "SUCCESS_LOAD_DATA", currentTime);

        } catch (Exception e) {
            // Gửi email thông báo lỗi
            sendEmail("Load data vào bảng " + tableStaging + " thất bại",
                    "Đã xảy ra lỗi trong quá trình tải dữ liệu: " + e.getMessage(),
                    "Load data thất bại", filePath);

            // Ghi log thất bại
            insertLog("config_id_example", filePath, "Load data vào bảng " + tableStaging + " thất bại",
                    "FAILURE_LOAD_DATA", currentTime);
        }
    }

    // Phương thức gửi email
    private void sendEmail(String subject, String body, String title, String filePath) {
        emailService.send("phamtrungtinpy363@gmail.com", subject, body);
    }

    // Phương thức ghi log
    private void insertLog(String configId, String filePath, String message, String status, LocalDateTime currentTime) {
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO control.logs (config_id, file_path, message, status, timestamp) VALUES (?, ?, ?, ?, ?)")) {

            ps.setString(1, configId);
            ps.setString(2, filePath);
            ps.setString(3, message);
            ps.setString(4, status);

            // Chuyển đổi LocalDateTime thành Timestamp
            ps.setTimestamp(5, Timestamp.valueOf(currentTime));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


