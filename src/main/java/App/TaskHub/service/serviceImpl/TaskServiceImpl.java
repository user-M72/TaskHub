package App.TaskHub.service.serviceImpl;

import App.TaskHub.dto.req.task.TaskRequest;
import App.TaskHub.dto.res.task.TaskResponse;
import App.TaskHub.entity.Task;
import App.TaskHub.entity.User;
import App.TaskHub.mapper.TaskMapper;
import App.TaskHub.repository.TaskRepository;
import App.TaskHub.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<TaskResponse> get() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse getById(UUID id) {
        Task task = repository.findById(id)
                .orElseThrow(()->new RuntimeException("Task not found by id: " + id));
        return mapper.toDto(task);
    }

    @Override
    public TaskResponse created(TaskRequest request) {
        Task task = mapper.toEntity(request);

        if (request.assigneeId()!=null){
            User assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(()-> new RuntimeException("Assignee not found by id: " + request.assigneeId()));
            task.setAssignee(assignee);
        }

        if (request.creatorId() != null){
            User creator = userRepository.findById(request.creatorId())
                    .orElseThrow(()->new RuntimeException("Creator not found by id: " + request.creatorId()));
            task.setCreator(creator);
        }

        return mapper.toDto(repository.save(task));
    }

    @Override
    public TaskResponse updated(UUID id, TaskRequest request) {
        Task task = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Task not found by id: " + id));

        mapper.updateFromDto(request, task);
        return mapper.toDto(repository.save(task));
    }

    @Override
    public void deleted(UUID id) {
        if (!repository.existsById(id)){
            throw new RuntimeException("Task not found by id: " + id);
        }
        repository.deleteById(id);
    }
}
