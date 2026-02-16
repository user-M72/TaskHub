package app.TaskHub.repository;

import app.TaskHub.entity.Role;
import app.TaskHub.entity.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(Roles name);
}
