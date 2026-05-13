package app.infrastructure.adapter.sql.repository;

import app.domain.enums.SystemRole;
import app.domain.enums.UserStatus;
import app.infrastructure.adapter.sql.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByRelatedClientId(UUID relatedClientId);

    boolean existsByUsername(String username);

    List<UserEntity> findBySystemRole(SystemRole systemRole);

    List<UserEntity> findByUserStatus(UserStatus userStatus);
}