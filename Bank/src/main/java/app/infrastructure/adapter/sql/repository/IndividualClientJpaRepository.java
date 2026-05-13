package app.infrastructure.adapter.sql.repository;

import app.infrastructure.adapter.sql.entity.IndividualClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IndividualClientJpaRepository extends JpaRepository<IndividualClientEntity, UUID> {
}