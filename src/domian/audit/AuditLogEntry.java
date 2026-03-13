package ConstruccionDeSoftwareII.src.domian.audit;
import java.time.LocalDateTime;

/**
    Member develop: Maria Jose Lopera Velasquez
    Version 1.0
 */

public class AuditLogEntry {

    private String logID; // Unique
    private String operationType;
    private LocalDateTime operationDateTime;
    private int userID;
    private String userRole;
    private String affectedProductID;
    private Object detailData;

    public AuditLogEntry(String logID,
                         String operationType,
                         LocalDateTime operationDateTime,
                         int userID,
                         String userRole,
                         String affectedProductID,
                         Object detailData) {
        this.logID = logID;
        this.operationType = operationType;
        this.operationDateTime = operationDateTime;
        this.userID = userID;
        this.userRole = userRole;
        this.affectedProductID = affectedProductID;
        this.detailData = detailData;
    }

    public String getLogID() {
        return logID;
    }

    public void setLogID(String logID) {
        this.logID = logID;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public LocalDateTime getOperationDateTime() {
        return operationDateTime;
    }

    public void setOperationDateTime(LocalDateTime operationDateTime) {
        this.operationDateTime = operationDateTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getAffectedProductID() {
        return affectedProductID;
    }

    public void setAffectedProductID(String affectedProductID) {
        this.affectedProductID = affectedProductID;
    }

    public Object getDetailData() {
        return detailData;
    }

    public void setDetailData(Object detailData) {
        this.detailData = detailData;
    }
}