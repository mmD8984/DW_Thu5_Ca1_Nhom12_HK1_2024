package Model;

import java.security.Timestamp;

public class Config {
    private String idConfig;
    private String filePath;
    private String fieldsTerminated;
    private String optionallyEnclosed;
    private String linesTerminated;
    private Integer ignoreRow;
    private String stgFields;
    private Status status;  // Dùng enum Status
    private Timestamp createTime;

    // Constructor
    public Config(String idConfig, String filePath, String fieldsTerminated, String optionallyEnclosed,
                  String linesTerminated, Integer ignoreRow, String stgFields, Status status, Timestamp createTime) {
        this.idConfig = idConfig;
        this.filePath = filePath;
        this.fieldsTerminated = fieldsTerminated;
        this.optionallyEnclosed = optionallyEnclosed;
        this.linesTerminated = linesTerminated;
        this.ignoreRow = ignoreRow;
        this.stgFields = stgFields;
        this.status = status;
        this.createTime = createTime;
    }

    // Getters and Setters
    public String getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(String idConfig) {
        this.idConfig = idConfig;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFieldsTerminated() {
        return fieldsTerminated;
    }

    public void setFieldsTerminated(String fieldsTerminated) {
        this.fieldsTerminated = fieldsTerminated;
    }

    public String getOptionallyEnclosed() {
        return optionallyEnclosed;
    }

    public void setOptionallyEnclosed(String optionallyEnclosed) {
        this.optionallyEnclosed = optionallyEnclosed;
    }

    public String getLinesTerminated() {
        return linesTerminated;
    }

    public void setLinesTerminated(String linesTerminated) {
        this.linesTerminated = linesTerminated;
    }

    public Integer getIgnoreRow() {
        return ignoreRow;
    }

    public void setIgnoreRow(Integer ignoreRow) {
        this.ignoreRow = ignoreRow;
    }

    public String getStgFields() {
        return stgFields;
    }

    public void setStgFields(String stgFields) {
        this.stgFields = stgFields;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

}
