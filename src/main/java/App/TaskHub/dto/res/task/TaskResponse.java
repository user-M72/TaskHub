package App.TaskHub.dto.res.task;


import App.TaskHub.entity.enums.TaskPriority;
import App.TaskHub.entity.enums.TaskStatus;

import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        String project,
        UUID assigneeId,
        UUID creatorId

) {}
