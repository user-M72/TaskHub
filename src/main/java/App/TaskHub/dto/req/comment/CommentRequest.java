package App.TaskHub.dto.req.comment;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentRequest(
        String content,
        UUID taskId,
        UUID userId,
        LocalDateTime createdAt
){}
