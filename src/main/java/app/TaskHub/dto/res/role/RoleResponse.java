package app.TaskHub.dto.res.role;

import java.util.UUID;

public record RoleResponse(
        UUID id,
        String name,
        String description
) {
}
