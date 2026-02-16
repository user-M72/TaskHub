package app.TaskHub.mapper;

import app.TaskHub.dto.req.task.TaskRequest;
import app.TaskHub.dto.res.task.TaskResponse;
import app.TaskHub.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapperHelper.class})
public interface TaskMapper {

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "creatorId", source = "creator.id")
    TaskResponse toDto(Task task);

    Task toEntity(TaskRequest request);

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "creator", source = "creatorId")
    void updateFromDto(TaskRequest request, @MappingTarget Task task);
}
