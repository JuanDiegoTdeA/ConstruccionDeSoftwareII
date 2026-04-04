package app.application.usecase;

import app.application.ports.in.CreateTransferUseCase;
import app.application.ports.out.AuditLogPort;
import app.application.ports.out.BankAccountRepositoryPort;
import app.application.ports.out.TransferRepositoryPort;
import app.application.ports.out.UserRepositoryPort;
import app.domain.models.BankAccount;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.shared.Exceptions.InsufficientFundsException;
import app.shared.Exceptions.InvalidStateTransitionException;
import app.shared.Exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public class CreateTransferService implements CreateTransferUseCase {

    private final TransferRepositoryPort transferRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final BankAccountRepositoryPort bankAccountRepositoryPort;
    private final AuditLogPort auditLogPort;

    public CreateTransferService(TransferRepositoryPort transferRepositoryPort,
                                 UserRepositoryPort userRepositoryPort,
                                 BankAccountRepositoryPort bankAccountRepositoryPort,
                                 AuditLogPort auditLogPort) {
        this.transferRepositoryPort    = transferRepositoryPort;
        this.userRepositoryPort        = userRepositoryPort;
        this.bankAccountRepositoryPort = bankAccountRepositoryPort;
        this.auditLogPort              = auditLogPort;
    }

    @Override
    public Transfer createTransfer(String sourceAccountNumber,
                                   String destinationAccountNumber,
                                   BigDecimal amount,
                                   UUID createdByUserId,
                                   LocalDateTime creationDateTime,
                                   BigDecimal approvalThreshold) {

        User creator = userRepositoryPort.findById(createdByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", createdByUserId));


        BankAccount source = bankAccountRepositoryPort
                .findByAccountNumber(sourceAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "BankAccount", sourceAccountNumber));


        BankAccount destination = bankAccountRepositoryPort
                .findByAccountNumber(destinationAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "BankAccount", destinationAccountNumber));


        Transfer transfer = Transfer.create(null, source, destination,
                                            amount, creationDateTime, creator);


        if (transfer.requiresApproval(approvalThreshold)) {
            return handlePendingApproval(transfer, createdByUserId, approvalThreshold);
        } else {
            return handleDirectExecution(transfer, source, destination, createdByUserId);
        }
    }

    private Transfer handlePendingApproval(Transfer transfer,
                                           UUID createdByUserId,
                                           BigDecimal approvalThreshold) {
        // Transfer stays in PENDING_APPROVAL — no fund movement
        Transfer savedTransfer = transferRepositoryPort.save(transfer);

        auditLogPort.save(
                "Transfer",
                String.valueOf(savedTransfer.getTransferId()),
                "TRANSFER_PENDING_APPROVAL",
                createdByUserId.toString(),
                LocalDateTime.now(),
                "amount=" + savedTransfer.getAmount()
                + ", approvalThreshold=" + approvalThreshold
                + ", sourceAccount=" + savedTransfer.getSourceAccount().getAccountNumber()
                + ", destinationAccount=" + savedTransfer.getDestinationAccount().getAccountNumber()
        );

        return savedTransfer;
    }

    private Transfer handleDirectExecution(Transfer transfer,
                                           BankAccount source,
                                           BankAccount destination,
                                           UUID createdByUserId) {

        BigDecimal sourceBalanceBefore      = source.getCurrentBalance();
        BigDecimal destinationBalanceBefore = destination.getCurrentBalance();


        try {
            transfer.execute();
        } catch (IllegalStateException e) {
            String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (msg.contains("insufficient")) {
                throw new InsufficientFundsException(
                        source.getAccountNumber(),
                        sourceBalanceBefore,
                        transfer.getAmount());
            }
            throw new InvalidStateTransitionException(
                    "Transfer", transfer.getTransferStatus(), "execute");
        }


        bankAccountRepositoryPort.save(source);
        bankAccountRepositoryPort.save(destination);

        // Persist executed transfer
        Transfer savedTransfer = transferRepositoryPort.save(transfer);

        // Audit with balance snapshot (PDF requirement)
        auditLogPort.save(
                "Transfer",
                String.valueOf(savedTransfer.getTransferId()),
                "TRANSFER_EXECUTED",
                createdByUserId.toString(),
                LocalDateTime.now(),
                "amount=" + savedTransfer.getAmount()
                + ", sourceAccount=" + source.getAccountNumber()
                + ", sourceBalanceBefore=" + sourceBalanceBefore
                + ", sourceBalanceAfter=" + source.getCurrentBalance()
                + ", destinationAccount=" + destination.getAccountNumber()
                + ", destinationBalanceBefore=" + destinationBalanceBefore
                + ", destinationBalanceAfter=" + destination.getCurrentBalance()
        );

        return savedTransfer;
    }
}
