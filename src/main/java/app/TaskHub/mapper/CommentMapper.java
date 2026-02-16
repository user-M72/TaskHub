package app.TaskHub.mapper;

import app.TaskHub.dto.req.comment.CommentRequest;
import app.TaskHub.dto.res.comment.CommentResponse;
import app.TaskHub.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapperHelper.class, TaskMapperHelper.class})
public interface CommentMapper {

    @Mapping(source = "taskId", target = "task")
    @Mapping(source = "userId", target = "user")
    Comment toEntity(CommentRequest request);

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "user.id", target = "userId")
    CommentResponse toDto(Comment comment);

}
