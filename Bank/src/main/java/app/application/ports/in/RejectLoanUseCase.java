package app.application.ports.in;

import app.domain.models.Loan;

import java.util.UUID;


public interface RejectLoanUseCase {

    Loan rejectLoan(Long loanId, UUID approverId);
}