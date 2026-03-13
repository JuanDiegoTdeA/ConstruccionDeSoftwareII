package ConstruccionDeSoftwareII.src.domian.audit;

import java.time.LocalDateTime;

/**
    Member develop: Maria Jose Lopera Velasquez
    Version 1.0
 */


public class ExpirationDetail {

    private String reason;
    private LocalDateTime expirationDateTime;
    private int creatorUserID;

    public ExpirationDetail(String reason,
                            LocalDateTime expirationDateTime,
                            int creatorUserID) {
        this.reason = reason;
        this.expirationDateTime = expirationDateTime;
        this.creatorUserID = creatorUserID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(LocalDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    public int getCreatorUserID() {
        return creatorUserID;
    }

    public void setCreatorUserID(int creatorUserID) {
        this.creatorUserID = creatorUserID;
    }
}
