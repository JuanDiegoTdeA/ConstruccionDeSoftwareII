package app.application.ports.in;

import app.domain.models.Transfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public interface CreateTransferUseCase {

    Transfer createTransfer(String sourceAccountNumber,
                            String destinationAccountNumber,
                            BigDecimal amount,
                            UUID createdByUserId,
                            LocalDateTime creationDateTime,
                            BigDecimal approvalThreshold);
}