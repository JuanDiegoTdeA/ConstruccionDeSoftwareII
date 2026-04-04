package app.application.ports.in;

import app.domain.models.Transfer;

import java.time.LocalDateTime;
import java.util.UUID;


public interface ApproveTransferUseCase {

    Transfer approveTransfer(Long transferId,
                             UUID approverId,
                             LocalDateTime approvalDateTime);
}