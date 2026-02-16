package app.TaskHub.dto.req.task;

import app.TaskHub.entity.enums.TaskPriority;
import app.TaskHub.entity.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskRequest(
        @NotBlank String title,
        String description,
        TaskStatus status,
        @NotNull TaskPriority priority,
        String project,
        @NotNull LocalDateTime dueDate,
        @NotNull UUID assigneeId,
        @NotNull UUID creatorId
){}

