package app.application.ports.in;

import app.domain.enums.LoanType;
import app.domain.models.Loan;

import java.math.BigDecimal;
import java.util.UUID;


public interface RequestLoanUseCase {

    Loan requestLoan(UUID clientId,
                     LoanType loanType,
                     BigDecimal requestedAmount,
                     Integer termInMonths);
}
