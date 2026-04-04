package app.application.ports.in;

import java.time.LocalDateTime;

public interface RegisterAuditLogUseCase {

    void registerAuditLog(String entityType,
                          String entityId,
                          String action,
                          String performedBy,
                          LocalDateTime occurredAt,
                          String detail);
}
