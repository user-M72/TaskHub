package App.TaskHub.dto.req.task;

import App.TaskHub.entity.enums.TaskPriority;
import App.TaskHub.entity.enums.TaskStatus;

import java.util.UUID;

public record TaskRequest(

        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        String project,
        UUID assigneeId,
        UUID creatorId


){}

