package app.TaskHub.service.serviceImpl;

import app.TaskHub.dto.req.task.TaskRequest;
import app.TaskHub.dto.res.task.TaskResponse;
import app.TaskHub.entity.Task;
import app.TaskHub.entity.User;
import app.TaskHub.entity.enums.TaskPriority;
import app.TaskHub.entity.enums.TaskStatus;
import app.TaskHub.mapper.TaskMapper;
import app.TaskHub.repository.TaskRepository;
import app.TaskHub.repository.UserRepository;
import app.TaskHub.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final UserRepository userRepository;

    @Override
    public Page<TaskResponse> getForAssignee(UUID assigneeId, Pageable pageable) {
            return repository.findAllByAssigneeId(assigneeId, pageable).map(mapper::toDto);
    }

    @Override
    public Page<TaskResponse> getForCreator(UUID creatorId, Pageable pageable) {
            return repository.findAllByCreatorId(creatorId, pageable).map(mapper::toDto);
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

    @Override
    public Task updateStatus(UUID id, TaskStatus status) {
        Task task = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Task not found"));

        task.setStatus(status);
        return repository.save(task);
    }

    @Override
    public Task updatePriority(UUID id, TaskPriority priority) {
        Task task = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Task not found"));

        task.setPriority(priority);
        return repository.save(task);
    }
}
