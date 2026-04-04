package app.application.ports.in;

import app.domain.enums.AccountType;
import app.domain.enums.CurrencyType;
import app.domain.models.BankAccount;

import java.math.BigDecimal;
import java.util.UUID;


public interface OpenBankAccountUseCase {

    /**
     * @param clientId       UUID of the client who will own the account
     * @param accountNumber  unique account number assigned by the bank
     * @param accountType    savings, checking, or fixed-term
     * @param initialBalance opening balance; must be >= 0
     * @param currency       currency in which the account will operate
     * @return the newly created {@link BankAccount} in {@code ACTIVE} status
     */
    BankAccount openAccount(UUID clientId,
                            String accountNumber,
                            AccountType accountType,
                            BigDecimal initialBalance,
                            CurrencyType currency);
}