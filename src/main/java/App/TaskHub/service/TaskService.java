package App.TaskHub.service;

import App.TaskHub.dto.req.task.TaskRequest;
import App.TaskHub.dto.res.task.TaskResponse;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    List<TaskResponse> get();

    TaskResponse getById(UUID id);

    TaskResponse created(TaskRequest request);

    TaskResponse updated(UUID id, TaskRequest request);

    void deleted(UUID id);
}
