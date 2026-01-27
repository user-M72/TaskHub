package App.TaskHub.service;

import App.TaskHub.dto.req.task.TaskRequest;
import App.TaskHub.dto.res.task.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TaskService {

    Page<TaskResponse> get(Pageable pageable);

    TaskResponse getById(UUID id);

    TaskResponse created(TaskRequest request);

    TaskResponse updated(UUID id, TaskRequest request);

    void deleted(UUID id);
}
