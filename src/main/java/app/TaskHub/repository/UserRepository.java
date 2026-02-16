package app.TaskHub.repository;

import app.TaskHub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);
    boolean existsByPassword(String password);

    Optional<User> findByUsername(String username);

    // ДОБАВЬТЕ ЭТОТ МЕТОД если его нет
    boolean existsByEmail(String email);

    // Опционально: для более точной проверки
    // Optional<User> findByEmail(String email);
}
