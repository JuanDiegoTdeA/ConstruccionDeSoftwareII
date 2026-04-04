package app.domain.models;

import app.domain.enums.AccountStatus;
import app.domain.enums.LoanStatus;
import app.domain.enums.LoanType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Loan {
private final Long loanId;
    private final LoanType loanType;
    private final Client applicantClient;
    private final BigDecimal requestedAmount;
    private final Integer termInMonths;

    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private LoanStatus loanStatus;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private BankAccount disbursementTargetAccount;

    private Loan(Long loanId, LoanType loanType, Client applicantClient,
                 BigDecimal requestedAmount, Integer termInMonths) {
        this.loanId           = loanId;
        this.loanType         = loanType;
        this.applicantClient  = applicantClient;
        this.requestedAmount  = requestedAmount;
        this.termInMonths     = termInMonths;
        this.loanStatus       = LoanStatus.UNDER_REVIEW;
    }

    public static Loan request(Long loanId,
                               LoanType loanType,
                               Client applicantClient,
                               BigDecimal requestedAmount,
                               Integer termInMonths) {
        Objects.requireNonNull(loanId,           "loanId must not be null.");
        Objects.requireNonNull(loanType,         "loanType must not be null.");
        Objects.requireNonNull(applicantClient,  "applicantClient must not be null.");
        Objects.requireNonNull(requestedAmount,  "requestedAmount must not be null.");
        Objects.requireNonNull(termInMonths,     "termInMonths must not be null.");

        if (requestedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "requestedAmount must be > 0, got: " + requestedAmount);
        }
        if (termInMonths <= 0) {
            throw new IllegalArgumentException(
                    "termInMonths must be > 0, got: " + termInMonths);
        }

        return new Loan(loanId, loanType, applicantClient, requestedAmount, termInMonths);
    }


    public void approve(BigDecimal approvedAmount, BigDecimal interestRate, LocalDate approvalDate) {
        requireStatus(LoanStatus.UNDER_REVIEW, "approve");

        Objects.requireNonNull(approvedAmount, "approvedAmount must not be null.");
        Objects.requireNonNull(interestRate,   "interestRate must not be null.");
        Objects.requireNonNull(approvalDate,   "approvalDate must not be null.");

        if (approvedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "approvedAmount must be > 0, got: " + approvedAmount);
        }
        if (interestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "interestRate must be >= 0, got: " + interestRate);
        }
        if (approvalDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "approvalDate must not be a future date, got: " + approvalDate);
        }

        this.approvedAmount = approvedAmount;
        this.interestRate   = interestRate;
        this.approvalDate   = approvalDate;
        this.loanStatus     = LoanStatus.APPROVED;
    }


    public void reject() {
        requireStatus(LoanStatus.UNDER_REVIEW, "reject");
        this.loanStatus = LoanStatus.REJECTED;
    }


    public void disburse(LocalDate disbursementDate, BankAccount disbursementTargetAccount) {
        requireStatus(LoanStatus.APPROVED, "disburse");

        Objects.requireNonNull(disbursementDate,          "disbursementDate must not be null.");
        Objects.requireNonNull(disbursementTargetAccount, "disbursementTargetAccount must not be null.");

        if (disbursementDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "disbursementDate must not be a future date, got: " + disbursementDate);
        }
        if (disbursementTargetAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Disbursement target account must be ACTIVE, but is: "
                    + disbursementTargetAccount.getAccountStatus());
        }

        disbursementTargetAccount.deposit(approvedAmount);

        this.disbursementTargetAccount = disbursementTargetAccount;
        this.disbursementDate          = disbursementDate;
        this.loanStatus                = LoanStatus.DISBURSED;
    }


    public Long getLoanId()                              { return loanId; }
    public LoanType getLoanType()                        { return loanType; }
    public Client getApplicantClient()                   { return applicantClient; }
    public BigDecimal getRequestedAmount()               { return requestedAmount; }
    public BigDecimal getApprovedAmount()                { return approvedAmount; }
    public BigDecimal getInterestRate()                  { return interestRate; }
    public Integer getTermInMonths()                     { return termInMonths; }
    public LoanStatus getLoanStatus()                    { return loanStatus; }
    public LocalDate getApprovalDate()                   { return approvalDate; }
    public LocalDate getDisbursementDate()               { return disbursementDate; }
    public BankAccount getDisbursementTargetAccount()    { return disbursementTargetAccount; }

    private void requireStatus(LoanStatus required, String operation) {
        if (loanStatus != required) {
            throw new IllegalStateException(
                    "Cannot " + operation + ": loan is " + loanStatus
                    + ", expected " + required + ".");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan loan)) return false;
        return Objects.equals(loanId, loan.loanId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId);
    }

    @Override
    public String toString() {
        return "Loan{loanId=" + loanId + ", type=" + loanType
                + ", status=" + loanStatus + ", requestedAmount=" + requestedAmount + '}';
    }
}
