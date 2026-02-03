package App.TaskHub.dto.req;

import java.util.UUID;

public record GetTaskRequest(
        UUID assigneeId,
        UUID creatorId
) {
}
