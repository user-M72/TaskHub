package app.TaskHub.mapper;

import app.TaskHub.entity.User;
import app.TaskHub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapperHelper {

    @Autowired
    private UserRepository userRepository;

    public User toEntity(UUID id) {
        if (id == null) return null;
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found by id: " + id));
    }
}
