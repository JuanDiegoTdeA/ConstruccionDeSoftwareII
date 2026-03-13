package ConstruccionDeSoftwareII.src.domian.loan;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
    Member develop: Juan Diego Millan
    Version 1.0
 */
public class Loan {

    private int loanID; // PK
    private String loanType;
    private String applicantClientID; // FK → User.identificationID
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private int termMonths;
    private String loanStatus;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private String disbursementTargetAccount; // FK → BankAccount.accountNumber

    public Loan(int loanID,
                String loanType,
                String applicantClientID,
                BigDecimal requestedAmount,
                BigDecimal approvedAmount,
                BigDecimal interestRate,
                int termMonths,
                String loanStatus,
                LocalDate approvalDate,
                LocalDate disbursementDate,
                String disbursementTargetAccount) {
        this.loanID = loanID;
        this.loanType = loanType;
        this.applicantClientID = applicantClientID;
        this.requestedAmount = requestedAmount;
        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.loanStatus = loanStatus;
        this.approvalDate = approvalDate;
        this.disbursementDate = disbursementDate;
        this.disbursementTargetAccount = disbursementTargetAccount;
    }

    public int getLoanID() {
        return loanID;
    }

    public void setLoanID(int loanID) {
        this.loanID = loanID;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getApplicantClientID() {
        return applicantClientID;
    }

    public void setApplicantClientID(String applicantClientID) {
        this.applicantClientID = applicantClientID;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(int termMonths) {
        this.termMonths = termMonths;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public String getDisbursementTargetAccount() {
        return disbursementTargetAccount;
    }

    public void setDisbursementTargetAccount(String disbursementTargetAccount) {
        this.disbursementTargetAccount = disbursementTargetAccount;
    }
}