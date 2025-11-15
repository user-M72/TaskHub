package App.TaskHub.dto.req.task;

import App.TaskHub.entity.User;
import App.TaskHub.entity.enums.TaskPriority;
import App.TaskHub.entity.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskRequest(

        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        String project,
        User assigneeId,
        User creatorId,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
){}

