package app.infrastructure.adapter.rest.mapper;

import app.domain.models.Transfer;
import app.domain.models.User;
import app.infrastructure.adapter.rest.dto.transfer.TransferResponse;

import java.util.UUID;

/**
 * Static utility that converts a {@link Transfer} aggregate into its outbound
 * {@link TransferResponse} DTO. Enum-valued attributes are exposed using their
 * {@link Enum#name()} representation, and nullable lifecycle fields
 * ({@code approvalDateTime}, {@code approvedBy}) are propagated as {@code null}
 * — filtered out at serialization time by {@code @JsonInclude(NON_NULL)} on
 * the response record.
 */
public final class TransferDtoMapper {

    private TransferDtoMapper() {
        // Utility class — no instances.
    }

    /**
     * Maps a domain {@link Transfer} to a {@link TransferResponse}.
     *
     * @param transfer domain aggregate to map; must not be {@code null}
     * @return the corresponding response DTO
     */
    public static TransferResponse toResponse(Transfer transfer) {
        User approver = transfer.getApprovedBy();
        UUID approvedByUserId = approver != null ? approver.getUserId() : null;

        return new TransferResponse(
                transfer.getTransferId(),
                transfer.getSourceAccount().getAccountNumber(),
                transfer.getDestinationAccount().getAccountNumber(),
                transfer.getAmount(),
                transfer.getCreationDateTime(),
                transfer.getTransferStatus().name(),
                transfer.getCreatedBy().getUserId(),
                transfer.getApprovalDateTime(),
                approvedByUserId
        );
    }
}