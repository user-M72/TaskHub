package app.TaskHub.dto.res.login;

import java.util.Set;
import java.util.UUID;

public record LoginResponse(
        UUID id,
        String username,
        String email,
        Set<String> roles

) {
}
