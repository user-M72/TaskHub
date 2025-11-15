package App.TaskHub.service.serviceImpl;

import App.TaskHub.dto.req.task.TaskRequest;
import App.TaskHub.dto.res.task.TaskResponse;
import App.TaskHub.mapper.TaskMapper;
import App.TaskHub.repository.TaskRepository;
import App.TaskHub.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository repository;
    @Autowired
    private TaskMapper mapper;

    @Override
    public List<TaskResponse> get() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse getById(UUID id) {
        return null;
    }

    @Override
    public TaskResponse created(TaskRequest request) {
        return null;
    }

    @Override
    public TaskResponse updated(UUID id, TaskRequest request) {
        return null;
    }

    @Override
    public void deleted(UUID id) {

    }
}
