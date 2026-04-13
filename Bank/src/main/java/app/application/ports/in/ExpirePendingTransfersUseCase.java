package app.application.ports.in;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpirePendingTransfersUseCase {


    List<Long> expirePendingTransfers(LocalDateTime now, Duration expirationWindow);
}