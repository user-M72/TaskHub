package App.TaskHub.service;

import App.TaskHub.dto.req.task.TaskRequest;
import App.TaskHub.dto.res.task.TaskResponse;
import App.TaskHub.entity.Task;
import App.TaskHub.entity.enums.TaskPriority;
import App.TaskHub.entity.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TaskService {

    Page<TaskResponse> get(Pageable pageable);

    TaskResponse getById(UUID id);

    TaskResponse created(TaskRequest request);

    TaskResponse updated(UUID id, TaskRequest request);

    void deleted(UUID id);

    Task updateStatus(UUID id, TaskStatus status);

    Task updatePriority(UUID id, TaskPriority priority);
}
