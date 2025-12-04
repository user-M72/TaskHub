package App.TaskHub.mapper;

import App.TaskHub.entity.User;
import App.TaskHub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapperHelper {

    @Autowired
    private UserRepository userRepository;

    public User fromUUID(UUID id){
        if (id == null) return null;
        return userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found: " + id));
    }
}
