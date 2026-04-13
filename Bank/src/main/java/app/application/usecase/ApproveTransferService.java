package app.application.usecase;

import app.application.ports.in.ApproveTransferUseCase;
import app.application.ports.out.AuditLogPort;
import app.application.ports.out.BankAccountRepositoryPort;
import app.application.ports.out.TransferRepositoryPort;
import app.application.ports.out.UserRepositoryPort;
import app.domain.enums.SystemRole;
import app.domain.models.BankAccount;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.shared.Exceptions.InsufficientFundsException;
import app.shared.Exceptions.InvalidStateTransitionException;
import app.shared.Exceptions.ResourceNotFoundException;
import app.shared.Exceptions.UnauthorizedOperationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public class ApproveTransferService implements ApproveTransferUseCase {

    private final TransferRepositoryPort transferRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final BankAccountRepositoryPort bankAccountRepositoryPort;
    private final AuditLogPort auditLogPort;

    public ApproveTransferService(TransferRepositoryPort transferRepositoryPort,
                                  UserRepositoryPort userRepositoryPort,
                                  BankAccountRepositoryPort bankAccountRepositoryPort,
                                  AuditLogPort auditLogPort) {
        this.transferRepositoryPort    = transferRepositoryPort;
        this.userRepositoryPort        = userRepositoryPort;
        this.bankAccountRepositoryPort = bankAccountRepositoryPort;
        this.auditLogPort              = auditLogPort;
    }

    @Override
    public Transfer approveTransfer(Long transferId,
                                    UUID approverId,
                                    LocalDateTime approvalDateTime) {

        Transfer transfer = transferRepositoryPort.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer", transferId));


        User approver = userRepositoryPort.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException("User", approverId));


        SystemRole role = approver.getSystemRole();
        if (role != SystemRole.COMPANY_SUPERVISOR && role != SystemRole.BUSINESS_ADMIN) {
            throw new UnauthorizedOperationException(
                    "User " + approverId + " with role " + role
                    + " is not authorized to approve transfers. "
                    + "Required role: COMPANY_SUPERVISOR or BUSINESS_ADMIN.");
        }

        BankAccount source      = transfer.getSourceAccount();
        BankAccount destination = transfer.getDestinationAccount();
        BigDecimal sourceBalanceBefore      = source.getCurrentBalance();
        BigDecimal destinationBalanceBefore = destination.getCurrentBalance();

        try {
            transfer.approve(approver, approvalDateTime);
        } catch (IllegalStateException e) {
            String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (msg.contains("insufficient")) {
                throw new InsufficientFundsException(
                        source.getAccountNumber(),
                        sourceBalanceBefore,
                        transfer.getAmount());
            }
            throw new InvalidStateTransitionException(
                    "Transfer", transfer.getTransferStatus(), "approve");
        }

        // 6. Persist updated accounts
        bankAccountRepositoryPort.save(source);
        bankAccountRepositoryPort.save(destination);

        // 7. Persist updated transfer
        Transfer savedTransfer = transferRepositoryPort.save(transfer);

        // 8. Audit with balance snapshot (PDF requirement)
        auditLogPort.save(
                "Transfer",
                String.valueOf(savedTransfer.getTransferId()),
                "TRANSFER_APPROVED_EXECUTED",
                approverId.toString(),
                LocalDateTime.now(),
                "amount=" + savedTransfer.getAmount()
                + ", approverRole=" + role
                + ", sourceAccount=" + source.getAccountNumber()
                + ", sourceBalanceBefore=" + sourceBalanceBefore
                + ", sourceBalanceAfter=" + source.getCurrentBalance()
                + ", destinationAccount=" + destination.getAccountNumber()
                + ", destinationBalanceBefore=" + destinationBalanceBefore
                + ", destinationBalanceAfter=" + destination.getCurrentBalance()
                + ", approvalDateTime=" + approvalDateTime
        );

        return savedTransfer;
    }
}
