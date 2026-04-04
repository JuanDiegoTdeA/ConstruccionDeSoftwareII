package app.application.usecase;

import app.application.ports.in.OpenBankAccountUseCase;
import app.application.ports.out.AuditLogPort;
import app.application.ports.out.BankAccountRepositoryPort;
import app.application.ports.out.ClientRepositoryPort;
import app.application.ports.out.UserRepositoryPort;
import app.domain.enums.AccountType;
import app.domain.enums.CurrencyType;
import app.domain.enums.UserStatus;
import app.domain.models.BankAccount;
import app.domain.models.Client;
import app.domain.models.User;
import app.shared.Exceptions.ResourceNotFoundException;
import app.shared.Exceptions.UnauthorizedOperationException;
import app.shared.Exceptions.BusinessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OpenBankAccountService implements OpenBankAccountUseCase {

    private final ClientRepositoryPort clientRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final BankAccountRepositoryPort bankAccountRepositoryPort;
    private final AuditLogPort auditLogPort;

    public OpenBankAccountService(ClientRepositoryPort clientRepositoryPort,
                                  UserRepositoryPort userRepositoryPort,
                                  BankAccountRepositoryPort bankAccountRepositoryPort,
                                  AuditLogPort auditLogPort) {
        this.clientRepositoryPort     = clientRepositoryPort;
        this.userRepositoryPort       = userRepositoryPort;
        this.bankAccountRepositoryPort = bankAccountRepositoryPort;
        this.auditLogPort             = auditLogPort;
    }

    @Override
    public BankAccount openAccount(UUID clientId,
                                   String accountNumber,
                                   AccountType accountType,
                                   BigDecimal initialBalance,
                                   CurrencyType currency) {

        // 1. Client must exist
        Client client = clientRepositoryPort.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", clientId));

        // 2. A system user linked to this client must exist
        User relatedUser = userRepositoryPort.findByRelatedClientId(clientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "System user linked to client", clientId));

        // 3. User must not be INACTIVE
        if (relatedUser.getUserStatus() == UserStatus.INACTIVE) {
            throw new UnauthorizedOperationException(
                    "Cannot open account: linked user " + relatedUser.getUserId()
                    + " is INACTIVE.");
        }

        // 4. User must not be BLOCKED
        if (relatedUser.getUserStatus() == UserStatus.BLOCKED) {
            throw new UnauthorizedOperationException(
                    "Cannot open account: linked user " + relatedUser.getUserId()
                    + " is BLOCKED.");
        }

        // 5. Account number must be unique
        if (bankAccountRepositoryPort.existsByAccountNumber(accountNumber)) {
            throw new BusinessException(
                    "Account number already exists: " + accountNumber);
        }

        // 6. Create the account via the domain factory method
        BankAccount account = BankAccount.open(accountNumber, accountType, client,
                                               initialBalance, currency);

        // 7. Persist
        BankAccount savedAccount = bankAccountRepositoryPort.save(account);

        // 8. Audit
        auditLogPort.save(
                "BankAccount",
                savedAccount.getAccountNumber(),
                "ACCOUNT_OPENED",
                relatedUser.getUserId().toString(),
                LocalDateTime.now(),
                "clientId=" + clientId
                + ", type=" + accountType
                + ", currency=" + currency
                + ", initialBalance=" + initialBalance
        );

        return savedAccount;
    }
}
