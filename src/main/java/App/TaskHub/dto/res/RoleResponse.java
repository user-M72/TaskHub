package App.TaskHub.dto.res;

import java.util.UUID;

public record RoleResponse(
        UUID id,
        String name,
        String description
) {
}
