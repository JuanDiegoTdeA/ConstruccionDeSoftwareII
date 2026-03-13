package ConstruccionDeSoftwareII.src.domian.transfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
    Member develop: Juan Diego Millan
    Version 1.0
 */

public class Transfer {

    private int transferID; // PK
    private String sourceAccount; // FK → BankAccount.accountNumber
    private String destinationAccount; // FK → BankAccount.accountNumber
    private BigDecimal amount;
    private LocalDateTime creationDate;
    private LocalDateTime approvalDate;
    private String transferStatus;
    private int createdByUserID; // FK → User.userID
    private int approvedByUserID; // FK → User.userID

    public Transfer(int transferID,
                    String sourceAccount,
                    String destinationAccount,
                    BigDecimal amount,
                    LocalDateTime creationDate,
                    LocalDateTime approvalDate,
                    String transferStatus,
                    int createdByUserID,
                    int approvedByUserID) {
        this.transferID = transferID;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.creationDate = creationDate;
        this.approvalDate = approvalDate;
        this.transferStatus = transferStatus;
        this.createdByUserID = createdByUserID;
        this.approvedByUserID = approvedByUserID;
    }

    public int getTransferID() {
        return transferID;
    }

    public void setTransferID(int transferID) {
        this.transferID = transferID;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public int getCreatedByUserID() {
        return createdByUserID;
    }

    public void setCreatedByUserID(int createdByUserID) {
        this.createdByUserID = createdByUserID;
    }

    public int getApprovedByUserID() {
        return approvedByUserID;
    }

    public void setApprovedByUserID(int approvedByUserID) {
        this.approvedByUserID = approvedByUserID;
    }
}
