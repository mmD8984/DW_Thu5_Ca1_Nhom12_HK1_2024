package Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Log {
    private int configId;
    private LogLevel logLevel;  // Dùng enum LogLevel
    private String filePath;
    private int isSuccess;
    private String action;
    private LocalDateTime timestamp;
    private String message;
    private String errorDetails;
    private Status status;  // Dùng enum Status
    private String user;
    private Timestamp createdAt;

    // Constructor
    public Log(int configId, LogLevel logLevel, String filePath, int isSuccess, String action, LocalDateTime timestamp,
               String message, String errorDetails, Status status, String user, Timestamp createdAt) {
        this.configId = configId;
        this.logLevel = logLevel;
        this.filePath = filePath;
        this.isSuccess = isSuccess;
        this.action = action;
        this.timestamp = timestamp;
        this.message = message;
        this.errorDetails = errorDetails;
        this.status = status;
        this.user = user;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
