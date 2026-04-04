package app.domain.models;

import app.domain.enums.TransferStatus;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transfer {
    private final Long transferId;
    private final BankAccount sourceAccount;
    private final BankAccount destinationAccount;
    private final BigDecimal amount;
    private final LocalDateTime creationDateTime;
    private final User createdBy;

    private LocalDateTime approvalDateTime;
    private TransferStatus transferStatus;
    private User approvedBy;

    private Transfer(Long transferId,
                     BankAccount sourceAccount,
                     BankAccount destinationAccount,
                     BigDecimal amount,
                     LocalDateTime creationDateTime,
                     User createdBy) {
        this.transferId          = transferId;
        this.sourceAccount       = sourceAccount;
        this.destinationAccount  = destinationAccount;
        this.amount              = amount;
        this.creationDateTime    = creationDateTime;
        this.createdBy           = createdBy;
        this.transferStatus      = TransferStatus.PENDING_APPROVAL;
    }


    public static Transfer create(Long transferId,
                                  BankAccount sourceAccount,
                                  BankAccount destinationAccount,
                                  BigDecimal amount,
                                  LocalDateTime creationDateTime,
                                  User createdBy) {
        Objects.requireNonNull(transferId,         "transferId must not be null.");
        Objects.requireNonNull(sourceAccount,      "sourceAccount must not be null.");
        Objects.requireNonNull(destinationAccount, "destinationAccount must not be null.");
        Objects.requireNonNull(amount,             "amount must not be null.");
        Objects.requireNonNull(creationDateTime,   "creationDateTime must not be null.");
        Objects.requireNonNull(createdBy,          "createdBy must not be null.");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be > 0, got: " + amount);
        }
        if (sourceAccount.equals(destinationAccount)) {
            throw new IllegalArgumentException(
                    "sourceAccount and destinationAccount must be different accounts.");
        }

        return new Transfer(transferId, sourceAccount, destinationAccount,
                            amount, creationDateTime, createdBy);
    }


    public boolean requiresApproval(BigDecimal threshold) {
        Objects.requireNonNull(threshold, "threshold must not be null.");
        if (threshold.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("threshold must be > 0, got: " + threshold);
        }
        return amount.compareTo(threshold) > 0;
    }


    public boolean isExpired(LocalDateTime now, Duration expirationWindow) {
        Objects.requireNonNull(now,              "now must not be null.");
        Objects.requireNonNull(expirationWindow, "expirationWindow must not be null.");
        if (transferStatus != TransferStatus.PENDING_APPROVAL) {
            return false;
        }
        return Duration.between(creationDateTime, now).compareTo(expirationWindow) > 0;
    }


    public void execute() {
        if (transferStatus == TransferStatus.EXECUTED
                || transferStatus == TransferStatus.REJECTED
                || transferStatus == TransferStatus.EXPIRED) {
            throw new IllegalStateException(
                    "Cannot execute: transfer is already " + transferStatus + ".");
        }
        if (!sourceAccount.canOperate()) {
            throw new IllegalStateException(
                    "Cannot execute: source account is " + sourceAccount.getAccountStatus() + ".");
        }
        if (!destinationAccount.canOperate()) {
            throw new IllegalStateException(
                    "Cannot execute: destination account is "
                    + destinationAccount.getAccountStatus() + ".");
        }


        sourceAccount.withdraw(amount);
        destinationAccount.deposit(amount);

        this.transferStatus = TransferStatus.EXECUTED;
    }


    public void approve(User approver, LocalDateTime approvalDateTime) {
        requirePendingApproval("approve");
        Objects.requireNonNull(approver,         "approver must not be null.");
        Objects.requireNonNull(approvalDateTime, "approvalDateTime must not be null.");

        this.approvedBy        = approver;
        this.approvalDateTime  = approvalDateTime;

        execute();
    }


    public void reject(User approver) {
        requirePendingApproval("reject");
        Objects.requireNonNull(approver, "approver must not be null.");

        this.approvedBy    = approver;
        this.transferStatus = TransferStatus.REJECTED;
    }


    public void expire() {
        requirePendingApproval("expire");
        this.transferStatus = TransferStatus.EXPIRED;
    }

    public Long getTransferId()                  { return transferId; }
    public BankAccount getSourceAccount()        { return sourceAccount; }
    public BankAccount getDestinationAccount()   { return destinationAccount; }
    public BigDecimal getAmount()                { return amount; }
    public LocalDateTime getCreationDateTime()   { return creationDateTime; }
    public LocalDateTime getApprovalDateTime()   { return approvalDateTime; }
    public TransferStatus getTransferStatus()    { return transferStatus; }
    public User getCreatedBy()                   { return createdBy; }
    public User getApprovedBy()                  { return approvedBy; }


    private void requirePendingApproval(String operation) {
        if (transferStatus != TransferStatus.PENDING_APPROVAL) {
            throw new IllegalStateException(
                    "Cannot " + operation + ": transfer is " + transferStatus
                    + ", expected PENDING_APPROVAL.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transfer transfer)) return false;
        return Objects.equals(transferId, transfer.transferId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId);
    }

    @Override
    public String toString() {
        return "Transfer{transferId=" + transferId
                + ", amount=" + amount
                + ", status=" + transferStatus
                + ", from='" + sourceAccount.getAccountNumber()
                + "', to='" + destinationAccount.getAccountNumber() + "'}";
    }
}
