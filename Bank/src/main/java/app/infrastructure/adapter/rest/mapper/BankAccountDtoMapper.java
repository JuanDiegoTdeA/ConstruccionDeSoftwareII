package app.infrastructure.adapter.rest.mapper;


import app.domain.models.BankAccount;
import app.infrastructure.adapter.rest.dto.account.BankAccountResponse;

/**
 * Static utility that converts a {@link BankAccount} aggregate into its
 * outbound {@link BankAccountResponse} DTO. Enum-valued attributes are
 * exposed using their {@link Enum#name()} representation.
 */
public final class BankAccountDtoMapper {

    private BankAccountDtoMapper() {
        // Utility class — no instances.
    }

    /**
     * Maps a domain {@link BankAccount} to a {@link BankAccountResponse}.
     *
     * @param account domain aggregate to map; must not be {@code null}
     * @return the corresponding response DTO
     */
    public static BankAccountResponse toResponse(BankAccount account) {
        return new BankAccountResponse(
                account.getAccountNumber(),
                account.getAccountType().name(),
                account.getHolder().getClientId(),
                account.getCurrentBalance(),
                account.getCurrency().name(),
                account.getOpeningDate(),
                account.getAccountStatus().name()
        );
    }
}