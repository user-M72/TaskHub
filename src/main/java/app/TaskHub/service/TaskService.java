package app.TaskHub.service;

import app.TaskHub.dto.req.task.TaskRequest;
import app.TaskHub.dto.res.task.TaskResponse;
import app.TaskHub.entity.Task;
import app.TaskHub.entity.enums.TaskPriority;
import app.TaskHub.entity.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TaskService {

    Page<TaskResponse> getForAssignee(UUID assigneeId, Pageable pageable);

    Page<TaskResponse> getForCreator(UUID creatorId, Pageable pageable);

    TaskResponse getById(UUID id);

    TaskResponse created(TaskRequest request);

    TaskResponse updated(UUID id, TaskRequest request);

    void deleted(UUID id);

    Task updateStatus(UUID id, TaskStatus status);

    Task updatePriority(UUID id, TaskPriority priority);
}
