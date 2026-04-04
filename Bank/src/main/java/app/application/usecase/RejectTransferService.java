package app.application.usecase;

import app.application.ports.in.RejectTransferUseCase;
import app.application.ports.out.AuditLogPort;
import app.application.ports.out.TransferRepositoryPort;
import app.application.ports.out.UserRepositoryPort;
import app.domain.enums.SystemRole;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.shared.Exceptions.InvalidStateTransitionException;
import app.shared.Exceptions.ResourceNotFoundException;
import app.shared.Exceptions.UnauthorizedOperationException;

import java.time.LocalDateTime;
import java.util.UUID;


public class RejectTransferService implements RejectTransferUseCase {

    private final TransferRepositoryPort transferRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final AuditLogPort auditLogPort;

    public RejectTransferService(TransferRepositoryPort transferRepositoryPort,
                                 UserRepositoryPort userRepositoryPort,
                                 AuditLogPort auditLogPort) {
        this.transferRepositoryPort = transferRepositoryPort;
        this.userRepositoryPort     = userRepositoryPort;
        this.auditLogPort           = auditLogPort;
    }

    @Override
    public Transfer rejectTransfer(Long transferId, UUID approverId) {

        // 1. Transfer must exist
        Transfer transfer = transferRepositoryPort.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer", transferId));

        // 2. Rejecting user must exist
        User approver = userRepositoryPort.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException("User", approverId));

        // 3. Only COMPANY_SUPERVISOR or BUSINESS_ADMIN may reject transfers
        SystemRole role = approver.getSystemRole();
        if (role != SystemRole.COMPANY_SUPERVISOR && role != SystemRole.BUSINESS_ADMIN) {
            throw new UnauthorizedOperationException(
                    "User " + approverId + " with role " + role
                    + " is not authorized to reject transfers. "
                    + "Required role: COMPANY_SUPERVISOR or BUSINESS_ADMIN.");
        }

        // 4. Apply domain state transition — translate IllegalStateException so the
        //    application layer speaks consistently in domain exceptions
        try {
            transfer.reject(approver);
        } catch (IllegalStateException e) {
            throw new InvalidStateTransitionException(
                    "Transfer", transfer.getTransferStatus(), "reject");
        }

        // 5. Persist
        Transfer savedTransfer = transferRepositoryPort.save(transfer);

        // 6. Audit
        auditLogPort.save(
                "Transfer",
                String.valueOf(savedTransfer.getTransferId()),
                "TRANSFER_REJECTED",
                approverId.toString(),
                LocalDateTime.now(),
                "rejectorRole=" + role
                + ", sourceAccount=" + savedTransfer.getSourceAccount().getAccountNumber()
                + ", destinationAccount=" + savedTransfer.getDestinationAccount().getAccountNumber()
                + ", amount=" + savedTransfer.getAmount()
        );

        return savedTransfer;
    }
}
