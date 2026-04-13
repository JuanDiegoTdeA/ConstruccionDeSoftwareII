package app.application.ports.in;

import app.domain.models.Loan;

import java.time.LocalDate;
import java.util.UUID;


public interface DisburseLoanUseCase {

    Loan disburseLoan(Long loanId,
                      String destinationAccountNumber,
                      LocalDate disbursementDate,
                      UUID analystUserId);
}
