package App.TaskHub.dto.res;

import App.TaskHub.entity.Role;

import java.util.Set;
import java.util.UUID;

public record UserResponse(

        UUID id,
        String firstName,
        String lastName,
        String username,
        String password,
        String phoneNumber,
        String email,
        Set<Role> roles

) {}
