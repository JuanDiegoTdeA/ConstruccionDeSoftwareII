package app.application.ports.in;

import app.domain.models.Loan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


public interface ApproveLoanUseCase {

    Loan approveLoan(Long loanId,
                     UUID approverId,
                     BigDecimal approvedAmount,
                     BigDecimal interestRate,
                     LocalDate approvalDate);
}