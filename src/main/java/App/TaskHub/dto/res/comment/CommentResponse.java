package App.TaskHub.dto.res.comment;

import App.TaskHub.entity.Task;
import App.TaskHub.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        String content,
        Task taskId,
        User userId,
        LocalDateTime createdAt
) {
}
