package app.infrastructure.adapter.mongo.repository;

import app.infrastructure.adapter.mongo.document.AuditLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {

    List<AuditLogDocument> findByEntityTypeAndEntityIdOrderByOccurredAtAsc(String entityType, String entityId);

    List<AuditLogDocument> findByPerformedByOrderByOccurredAtAsc(String performedBy);
}