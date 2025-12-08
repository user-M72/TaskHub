package App.TaskHub.dto.res.comment;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        String content,
        UUID taskId,
        UUID userId
) {
}
