package app.TaskHub.service;

import app.TaskHub.dto.req.comment.CommentRequest;
import app.TaskHub.dto.res.comment.CommentResponse;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    List<CommentResponse> get();

    CommentResponse getById(UUID id);

    CommentResponse created(CommentRequest request);

    CommentResponse updated(UUID id, CommentRequest request);

    void deleted(UUID id);


}
