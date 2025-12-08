package App.TaskHub.service.serviceImpl;

import App.TaskHub.dto.req.comment.CommentRequest;
import App.TaskHub.dto.res.comment.CommentResponse;
import App.TaskHub.dto.res.user.UserResponse;
import App.TaskHub.entity.Comment;
import App.TaskHub.entity.Task;
import App.TaskHub.entity.User;
import App.TaskHub.mapper.CommentMapper;
import App.TaskHub.repository.CommentRepository;
import App.TaskHub.repository.TaskRepository;
import App.TaskHub.repository.UserRepository;
import App.TaskHub.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository repository;
    @Autowired
    private CommentMapper mapper;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

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
        return null;
    }

    @Override
    public void deleted(UUID id) {
        if (!repository.existsById(id)){
            throw new RuntimeException("Comment not found by id: " + id);
        }
        repository.deleteById(id);
    }
}
