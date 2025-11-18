package App.TaskHub.dto.res.login;

import java.util.Set;

public record LoginResponse(
        String username,
        Set<String> roles

) {
}
