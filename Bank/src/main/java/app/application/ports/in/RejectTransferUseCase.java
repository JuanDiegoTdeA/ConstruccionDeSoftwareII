package app.application.ports.in;

import app.domain.models.Transfer;

import java.util.UUID;

public interface RejectTransferUseCase {

    Transfer rejectTransfer(Long transferId, UUID approverId);
}
