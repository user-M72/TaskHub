package App.TaskHub.service.serviceImpl;

import App.TaskHub.dto.req.comment.CommentRequest;
import App.TaskHub.dto.res.comment.CommentResponse;
import App.TaskHub.mapper.CommentMapper;
import App.TaskHub.repository.CommentRepository;
import App.TaskHub.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public CommentResponse created(CommentRequest request) {
        return null;
    }

    @Override
    public CommentResponse updated(UUID id, CommentRequest request) {
        return null;
    }

    @Override
    public Void deleted(UUID id) {
        return null;
    }
}
