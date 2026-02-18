package app.TaskHub.service;

import app.TaskHub.dto.req.comment.CommentRequest;
import app.TaskHub.dto.res.comment.CommentResponse;
import app.TaskHub.entity.Comment;
import app.TaskHub.entity.Task;
import app.TaskHub.entity.User;
import app.TaskHub.mapper.CommentMapper;
import app.TaskHub.repository.CommentRepository;
import app.TaskHub.repository.TaskRepository;
import app.TaskHub.repository.UserRepository;
import app.TaskHub.service.serviceImpl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    CommentServiceImpl testService;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper mapper;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;

    private final UUID commentId = UUID.randomUUID();
    private final UUID taskId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();

    private  Comment comment;
    private CommentRequest request;
    private CommentResponse expected;

    @BeforeEach
    void setup(){
        comment = new Comment();

        expected = new CommentResponse(
                commentId,
                "",
                taskId,
                userId

        );

        request = new CommentRequest(
                "",
                taskId,
                userId
        );
    }

    @Test
    void get_shouldWork() {
        List<Comment> comments = List.of(comment);

        when(commentRepository.findAll()).thenReturn(comments);
        when(mapper.toDto(comment)).thenReturn(expected);

        List<CommentResponse> actual = testService.get();

        assertEquals(List.of(expected), actual);

        verify(commentRepository).findAll();
        verify(mapper).toDto(comment);
    }

    @Test
    void getById_shouldWork() {

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(mapper.toDto(comment)).thenReturn(expected);

        CommentResponse actual = testService.getById(commentId);

        assertEquals(expected, actual);

        verify(commentRepository).findById(commentId);
        verify(mapper).toDto(comment);
    }
    
    @Test
    void getById_shouldThrow_whenCommentNotFound(){
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                ()-> testService.getById(commentId));

        verify(commentRepository).findById(commentId);
    }

    @Test
    void created_shouldWork() {
        Task task = new Task();
        User user = new User();

        CommentRequest request = new CommentRequest(
                "",
                taskId,
                userId
        );

        CommentResponse expected = new CommentResponse(
                commentId,
                "",
                taskId,
                userId
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.toEntity(request)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(mapper.toDto(comment)).thenReturn(expected);

        CommentResponse actual = testService.created(request);

        assertEquals(expected, actual);

        verify(taskRepository).findById(taskId);
        verify(userRepository).findById(userId);
        verify(mapper).toEntity(request);
        verify(commentRepository).save(comment);
        verify(mapper).toDto(comment);

    }

    @Test
    void updated_shouldWork() {

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(mapper.toDto(comment)).thenReturn(expected);

        CommentResponse actual = testService.updated(commentId, request);

        assertEquals(expected, actual);

        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(comment);
        verify(mapper).toDto(comment);

    }

    @Test
    void deleted_shouldWork(){

        when(commentRepository.existsById(commentId)).thenReturn(true);

        testService.deleted(commentId);

        verify(commentRepository).existsById(commentId);
        verify(commentRepository).deleteById(commentId);
    }

    @Test
    void deleted_shouldThrow_whenNotFound(){

        when(commentRepository.existsById(commentId)).thenReturn(false);

        assertThrows(RuntimeException.class,
                ()-> testService.deleted(commentId));

        verify(commentRepository).existsById(commentId);
        verify(commentRepository, never()).deleteById(commentId);
    }
}
