package app.application.usecase;

import app.application.ports.in.ExpirePendingTransfersUseCase;
import app.application.ports.out.AuditLogPort;
import app.application.ports.out.TransferRepositoryPort;
import app.domain.enums.TransferStatus;
import app.domain.models.Transfer;
import app.shared.Exceptions.InvalidStateTransitionException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ExpirePendingTransfersService implements ExpirePendingTransfersUseCase {

    private static final String EXPIRATION_REASON =
            "Vencida por falta de aprobación en el tiempo establecido";

    private final TransferRepositoryPort transferRepositoryPort;
    private final AuditLogPort auditLogPort;

    public ExpirePendingTransfersService(TransferRepositoryPort transferRepositoryPort,
                                         AuditLogPort auditLogPort) {
        this.transferRepositoryPort = transferRepositoryPort;
        this.auditLogPort           = auditLogPort;
    }

    @Override
    public List<Long> expirePendingTransfers(LocalDateTime now, Duration expirationWindow) {

        // 1. Load all transfers currently waiting for approval
        List<Transfer> pending = transferRepositoryPort
                .findByStatus(TransferStatus.PENDING_APPROVAL);

        List<Long> expiredIds = new ArrayList<>();

        for (Transfer transfer : pending) {

            // 2. Check whether this transfer has exceeded the expiration window
            if (!transfer.isExpired(now, expirationWindow)) {
                continue;
            }

            // 3. Apply domain state transition — translate IllegalStateException so the
            //    application layer speaks consistently in domain exceptions
            try {
                transfer.expire();
            } catch (IllegalStateException e) {
                throw new InvalidStateTransitionException(
                        "Transfer", transfer.getTransferStatus(), "expire");
            }

            // 4. Persist the expired transfer
            Transfer savedTransfer = transferRepositoryPort.save(transfer);

            // 5. Audit — include the reason mandated by the PDF spec
            auditLogPort.save(
                    "Transfer",
                    String.valueOf(savedTransfer.getTransferId()),
                    "TRANSFER_EXPIRED",
                    savedTransfer.getCreatedBy().getUserId().toString(),
                    now,
                    "motivo=" + EXPIRATION_REASON
                    + ", fechaVencimiento=" + now
                    + ", creationDateTime=" + savedTransfer.getCreationDateTime()
                    + ", sourceAccount=" + savedTransfer.getSourceAccount().getAccountNumber()
                    + ", amount=" + savedTransfer.getAmount()
            );

            expiredIds.add(savedTransfer.getTransferId());
        }

        return expiredIds;
    }
}
