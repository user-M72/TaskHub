package App.TaskHub.service.serviceImpl;

import App.TaskHub.dto.req.comment.CommentRequest;
import App.TaskHub.dto.res.comment.CommentResponse;
import App.TaskHub.entity.Comment;
import App.TaskHub.mapper.CommentMapper;
import App.TaskHub.repository.CommentRepository;
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
        Comment comment = mapper.toEntity(request);
        comment.setCreatedAt(LocalDateTime.now());
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
