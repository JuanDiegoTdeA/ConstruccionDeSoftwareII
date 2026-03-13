package ConstruccionDeSoftwareII.src.domian.account;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
    Member develop: Maria Jose Lopera Velasquez
    Version 1.0
 */

public class BankAccount {

    private String accountNumber; // PK
    private String accountType;
    private String holderID; // FK → User.identificationID
    private BigDecimal currentBalance;
    private String currency;
    private String accountStatus;
    private LocalDate openingDate;

    public BankAccount(String accountNumber,
                       String accountType,
                       String holderID,
                       BigDecimal currentBalance,
                       String currency,
                       String accountStatus,
                       LocalDate openingDate) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.holderID = holderID;
        this.currentBalance = currentBalance;
        this.currency = currency;
        this.accountStatus = accountStatus;
        this.openingDate = openingDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getHolderID() {
        return holderID;
    }

    public void setHolderID(String holderID) {
        this.holderID = holderID;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }
}