package App.TaskHub.mapper;

import App.TaskHub.dto.req.comment.CommentRequest;
import App.TaskHub.dto.res.comment.CommentResponse;
import App.TaskHub.entity.Comment;
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
