package App.TaskHub.mapper;

import App.TaskHub.dto.req.comment.CommentRequest;
import App.TaskHub.dto.res.comment.CommentResponse;
import App.TaskHub.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    CommentResponse toDto(Comment comment);

    Comment toEntity(CommentRequest request);

}
