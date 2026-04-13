package app.application.usecase;

import app.application.ports.in.ApproveLoanUseCase;
import app.application.ports.out.AuditLogPort;
import app.application.ports.out.LoanRepositoryPort;
import app.application.ports.out.UserRepositoryPort;
import app.domain.enums.SystemRole;
import app.domain.models.Loan;
import app.domain.models.User;
import app.shared.Exceptions.InvalidStateTransitionException;
import app.shared.Exceptions.ResourceNotFoundException;
import app.shared.Exceptions.UnauthorizedOperationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


public class ApproveLoanService implements ApproveLoanUseCase {

    private final LoanRepositoryPort loanRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final AuditLogPort auditLogPort;

    public ApproveLoanService(LoanRepositoryPort loanRepositoryPort,
                              UserRepositoryPort userRepositoryPort,
                              AuditLogPort auditLogPort) {
        this.loanRepositoryPort = loanRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.auditLogPort       = auditLogPort;
    }

    @Override
    public Loan approveLoan(Long loanId,
                            UUID approverId,
                            BigDecimal approvedAmount,
                            BigDecimal interestRate,
                            LocalDate approvalDate) {

        Loan loan = loanRepositoryPort.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", loanId));

        User approver = userRepositoryPort.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException("User", approverId));

        // 3. Approver must be INTERNAL_ANALYST — only this role may approve loans
        if (approver.getSystemRole() != SystemRole.INTERNAL_ANALYST) {
            throw new UnauthorizedOperationException(
                    "User " + approverId + " with role " + approver.getSystemRole()
                    + " is not authorized to approve loans. Required role: INTERNAL_ANALYST.");
        }

        try {
            loan.approve(approvedAmount, interestRate, approvalDate);
        } catch (IllegalStateException e) {
            throw new InvalidStateTransitionException(
                    "Loan", loan.getLoanStatus(), "approve");
        }

        // 5. Persist
        Loan savedLoan = loanRepositoryPort.save(loan);

        // 6. Audit
        auditLogPort.save(
                "Loan",
                String.valueOf(savedLoan.getLoanId()),
                "LOAN_APPROVED",
                approverId.toString(),
                LocalDateTime.now(),
                "approvedAmount=" + approvedAmount
                + ", interestRate=" + interestRate
                + ", approvalDate=" + approvalDate
                + ", approverRole=INTERNAL_ANALYST"
        );

        return savedLoan;
    }
}
