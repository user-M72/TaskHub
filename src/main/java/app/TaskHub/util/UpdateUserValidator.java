package app.TaskHub.util;

import app.TaskHub.entity.User;
import app.TaskHub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserValidator {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void validateAndSetUsername(User user, String username) {
        if (username != null && username.isEmpty()) {
            if (!user.getUsername().equals(username)) {
                if (userRepository.existsByUsername(username)) {
                    throw new RuntimeException("Username already exists");
                }
                user.setUsername(username);
            }
        }
    }

    public void validateAndSetEmail(User user, String email){
        if (email != null && !email.isEmpty()) {
            if (!user.getEmail().equals(email)) {
                if (userRepository.existsByEmail(email)) {
                    throw new RuntimeException("Email already exists");
                }
                user.setEmail(email);
            }
        }
    }

    public void validateAndSetPassword(User user, String currentPassword, String newPassword){
        if (currentPassword != null && newPassword != null) {

            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new RuntimeException("Current password is incorrect");
            }


            if (newPassword.length() < 6) {
                throw new RuntimeException("New password must be at least 6 characters long");
            }


            user.setPassword(passwordEncoder.encode(newPassword));
        }
    }
}
