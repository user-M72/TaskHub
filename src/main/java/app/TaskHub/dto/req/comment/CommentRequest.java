package app.TaskHub.dto.req.comment;

import java.util.UUID;

public record CommentRequest(
        String content,
        UUID taskId,
        UUID userId
){}
