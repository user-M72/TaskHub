package app.TaskHub.service.serviceImpl;

import app.TaskHub.dto.req.comment.CommentRequest;
import app.TaskHub.dto.res.comment.CommentResponse;
import app.TaskHub.entity.Comment;
import app.TaskHub.entity.Task;
import app.TaskHub.entity.User;
import app.TaskHub.mapper.CommentMapper;
import app.TaskHub.repository.CommentRepository;
import app.TaskHub.repository.TaskRepository;
import app.TaskHub.repository.UserRepository;
import app.TaskHub.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentResponse> get() {
        return repository.findAll().stream().map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse getById(UUID id) {
        Comment comment = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Comment not found by id: " + id));
        return mapper.toDto(comment);
    }

    @Override
    public CommentResponse created(CommentRequest request) {


        Task task = taskRepository.findById(request.taskId())
                .orElseThrow(()-> new RuntimeException("Task not found by id: " + request.taskId()));

        User user = userRepository.findById(request.userId())
                .orElseThrow(()-> new RuntimeException("User not found by id: " + request.userId()));

        Comment comment = mapper.toEntity(request);

        comment.setTask(task);
        comment.setUser(user);

        return mapper.toDto(repository.save(comment));
    }

    @Override
    public CommentResponse updated(UUID id, CommentRequest request) {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found by id: " + id));
        mapper.updateFromDto(request, comment);
        return mapper.toDto(repository.save(comment));
    }

    @Override
    public void deleted(UUID id) {
        if (!repository.existsById(id)){
            throw new RuntimeException("Comment not found by id: " + id);
        }
        repository.deleteById(id);
    }
}
