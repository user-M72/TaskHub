package App.TaskHub.dto.req;

import java.util.List;
import java.util.UUID;

public record UserRequest(

        String firstName,
        String lastName,
        String username,
        String password,
        String phoneNumber,
        String email,
        List<UUID> roleIds
) {}
