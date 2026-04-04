package app.domain.models;

import app.domain.enums.AccountStatus;
import app.domain.enums.AccountType;
import app.domain.enums.CurrencyType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;


public final class BankAccount {

    private final String accountNumber;
    private final AccountType accountType;
    private final Client holder;
    private final CurrencyType currency;
    private final LocalDate openingDate;

    private BigDecimal currentBalance;
    private AccountStatus accountStatus;

    private BankAccount(String accountNumber, AccountType accountType, Client holder,
                        BigDecimal initialBalance, CurrencyType currency,
                        LocalDate openingDate, AccountStatus accountStatus) {
        this.accountNumber  = accountNumber;
        this.accountType    = accountType;
        this.holder         = holder;
        this.currentBalance = initialBalance;
        this.currency       = currency;
        this.openingDate    = openingDate;
        this.accountStatus  = accountStatus;
    }


    public static BankAccount open(String accountNumber,
                                   AccountType accountType,
                                   Client holder,
                                   BigDecimal initialBalance,
                                   CurrencyType currency) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("accountNumber must not be blank.");
        }
        Objects.requireNonNull(accountType,    "accountType must not be null.");
        Objects.requireNonNull(holder,         "holder must not be null.");
        Objects.requireNonNull(currency,       "currency must not be null.");
        Objects.requireNonNull(initialBalance, "initialBalance must not be null.");

        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "initialBalance must be >= 0, got: " + initialBalance);
        }

        return new BankAccount(
                accountNumber.trim(),
                accountType,
                holder,
                initialBalance,
                currency,
                LocalDate.now(),
                AccountStatus.ACTIVE
        );
    }

   
    public boolean isActive() {
        return accountStatus == AccountStatus.ACTIVE;
    }

   
    public boolean canOperate() {
        return accountStatus == AccountStatus.ACTIVE;
    }


    public void deposit(BigDecimal amount) {
        requireOperable("deposit");
        requirePositiveAmount(amount, "deposit");
        currentBalance = currentBalance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        requireOperable("withdrawal");
        requirePositiveAmount(amount, "withdrawal");

        if (amount.compareTo(currentBalance) > 0) {
            throw new IllegalStateException(
                    "Insufficient balance. Available: " + currentBalance + ", requested: " + amount);
        }
        currentBalance = currentBalance.subtract(amount);
    }


    public void block() {
        if (accountStatus == AccountStatus.CANCELED) {
            throw new IllegalStateException("A canceled account cannot be blocked.");
        }
        accountStatus = AccountStatus.BLOCKED;
    }

    public void cancel() {
        if (accountStatus == AccountStatus.CANCELED) {
            throw new IllegalStateException("Account is already canceled.");
        }
        accountStatus = AccountStatus.CANCELED;
    }


    public String getAccountNumber()    { return accountNumber; }
    public AccountType getAccountType() { return accountType; }
    public Client getHolder()           { return holder; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public CurrencyType getCurrency()   { return currency; }
    public LocalDate getOpeningDate()   { return openingDate; }
    public AccountStatus getAccountStatus() { return accountStatus; }


    private void requireOperable(String operation) {
        if (!canOperate()) {
            throw new IllegalStateException(
                    "Cannot perform " + operation + ": account is " + accountStatus);
        }
    }

    private void requirePositiveAmount(BigDecimal amount, String operation) {
        Objects.requireNonNull(amount, operation + " amount must not be null.");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    operation + " amount must be > 0, got: " + amount);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount that)) return false;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return "BankAccount{number='" + accountNumber + "', type=" + accountType
                + ", holder='" + holder.getClientId() + "', balance=" + currentBalance
                + " " + currency + ", status=" + accountStatus + '}';
    }
}