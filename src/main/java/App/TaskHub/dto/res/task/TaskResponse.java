package App.TaskHub.dto.res.task;


import App.TaskHub.entity.enums.TaskPriority;
import App.TaskHub.entity.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        String project,
        LocalDateTime dueDate,
        UUID assigneeId,
        UUID creatorId

) {}
