package app.TaskHub.service;

import app.TaskHub.dto.req.task.TaskRequest;
import app.TaskHub.dto.res.task.TaskResponse;
import app.TaskHub.entity.Task;
import app.TaskHub.entity.User;
import app.TaskHub.entity.enums.TaskPriority;
import app.TaskHub.entity.enums.TaskStatus;
import app.TaskHub.mapper.TaskMapper;
import app.TaskHub.repository.TaskRepository;
import app.TaskHub.repository.UserRepository;
import app.TaskHub.service.serviceImpl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    TaskServiceImpl testService;

    @Mock
    private TaskRepository repository;
    @Mock
    private TaskMapper mapper;
    @Mock
    private UserRepository userRepository;

    private TaskResponse response;
    private TaskRequest request;
    private Task task;
    private User user;

    private final UUID taskId = UUID.randomUUID();
    private final UUID assigneeId = UUID.randomUUID();
    private final UUID creatorId = UUID.randomUUID();

    @BeforeEach
    void setUp(){

        response = new TaskResponse(
                taskId,
                "",
                "",
                TaskStatus.NEW,
                TaskPriority.HIGH,
                "",
                LocalDateTime.now().plusDays(3),
                assigneeId,
                creatorId
        );

        request = new TaskRequest(
                "",
                "",
                TaskStatus.NEW,
                TaskPriority.HIGH,
                "",
                LocalDateTime.now().plusDays(3),
                assigneeId,
                creatorId
        );

        task = new Task();
        user = new User();
    }

    @Test
    void getForAssignee_shouldWork(){
        Pageable pageable = PageRequest.of(0, 10);

        Page<Task> taskPage = new PageImpl<>(List.of(task));
        when(repository.findAllByAssigneeId(assigneeId, pageable)).thenReturn(taskPage);
        when(mapper.toDto(task)).thenReturn(response);

        Page<TaskResponse> actual = testService.getForAssignee(assigneeId, pageable);

        assertNotNull(actual);
        assertEquals(1, actual.getTotalElements());
        assertEquals(response, actual.getContent().getFirst());

        verify(repository).findAllByAssigneeId(assigneeId, pageable);
        verify(mapper).toDto(task);
    }

    @Test
    void getForCreator_shouldWork(){
        Pageable pageable = PageRequest.of(0, 10);

        Page<Task> taskPage = new PageImpl<>(List.of(task));
        when(repository.findAllByCreatorId(creatorId, pageable)).thenReturn(taskPage);
        when(mapper.toDto(task)).thenReturn(response);

        Page<TaskResponse> actual = testService.getForCreator(creatorId, pageable);

        assertNotNull(actual);
        assertEquals(1, actual.getTotalElements());
        assertEquals(response, actual.getContent().getFirst());

        verify(repository).findAllByCreatorId(creatorId, pageable);
        verify(mapper).toDto(task);
    }

    @Test
    void getById_shouldWork(){

        when(repository.findById(taskId)).thenReturn(Optional.of(task));
        when(mapper.toDto(task)).thenReturn(response);

        TaskResponse actual = testService.getById(taskId);

        assertEquals(response, actual);

        verify(repository).findById(taskId);
        verify(mapper).toDto(task);
    }

    @Test
    void getById_shouldThrow_whenTaskNotFound(){

        when(repository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                ()->testService.getById(taskId));

        verify(repository).findById(taskId);
    }

    @Test
    void created_shouldWork(){

        when(mapper.toEntity(request)).thenReturn(task);
        when(userRepository.findById(request.assigneeId())).thenReturn(Optional.of(user));
        when(userRepository.findById(request.creatorId())).thenReturn(Optional.of(user));
        when(repository.save(task)).thenReturn(task);
        when(mapper.toDto(task)).thenReturn(response);

        TaskResponse actual = testService.created(request);

        assertEquals(response, actual);

        verify(mapper).toEntity(request);
        verify(userRepository).findById(request.assigneeId());
        verify(userRepository).findById(request.creatorId());
        verify(repository).save(task);
        verify(mapper).toDto(task);
    }

    @Test
    void created_shouldThrow_whenAssigneeNotFound(){

        when(mapper.toEntity(request)).thenReturn(task);
        when(userRepository.findById(request.assigneeId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                ()-> testService.created(request));

        verify(mapper).toEntity(request);
        verify(userRepository).findById(request.assigneeId());
        verify(repository, never()).save(any());
    }

    @Test
    void created_shouldThrow_whenCreatorNotFound(){

        when(mapper.toEntity(request)).thenReturn(task);
        when(userRepository.findById(request.assigneeId())).thenReturn(Optional.of(user));
        when(userRepository.findById(request.creatorId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                ()-> testService.created(request));

        verify(mapper).toEntity(request);
        verify(userRepository).findById(request.assigneeId());
        verify(userRepository).findById(request.creatorId());
        verify(repository, never()).save(any());
    }

    @Test
    void created_shouldWork_withoutAssignee(){

        TaskRequest assigneeNullRequest = new TaskRequest(
                "",
                "",
                TaskStatus.NEW,
                TaskPriority.HIGH,
                "",
                LocalDateTime.now().plusDays(3),
                null,
                creatorId
        );

        when(mapper.toEntity(assigneeNullRequest)).thenReturn(task);
        when(userRepository.findById(assigneeNullRequest.creatorId())).thenReturn(Optional.of(user));
        when(repository.save(task)).thenReturn(task);
        when(mapper.toDto(task)).thenReturn(response);

        TaskResponse actual = testService.created(assigneeNullRequest);

        assertEquals(response, actual);

        verify(mapper).toEntity(assigneeNullRequest);
        verify(userRepository, never()).findById(assigneeNullRequest.assigneeId());
        verify(userRepository).findById(assigneeNullRequest.creatorId());
        verify(repository).save(task);
    }

    @Test
    void created_shouldWork_withoutCreator(){

        TaskRequest creatorNullRequest = new TaskRequest(
                "",
                "",
                TaskStatus.NEW,
                TaskPriority.HIGH,
                "",
                LocalDateTime.now().plusDays(3),
                assigneeId,
                null
        );

        when(mapper.toEntity(creatorNullRequest)).thenReturn(task);
        when(userRepository.findById(creatorNullRequest.assigneeId())).thenReturn(Optional.of(user));
        when(repository.save(task)).thenReturn(task);
        when(mapper.toDto(task)).thenReturn(response);

        TaskResponse actual = testService.created(creatorNullRequest);

        assertEquals(response, actual);

        verify(mapper).toEntity(creatorNullRequest);
        verify(userRepository).findById(creatorNullRequest.assigneeId());
        verify(userRepository, never()).findById(creatorNullRequest.creatorId());
        verify(repository).save(task);
    }

    @Test
    void updated_shouldWork(){

        when(repository.findById(taskId)).thenReturn(Optional.of(task));
        when(repository.save(task)).thenReturn(task);
        when(mapper.toDto(task)).thenReturn(response);

        TaskResponse actual = testService.updated(taskId, request);

        assertEquals(response, actual);

        verify(repository).findById(taskId);
        verify(repository).save(task);
        verify(mapper).toDto(task);
    }

    @Test
    void deleted_shouldWork(){

        when(repository.existsById(taskId)).thenReturn(true);

        testService.deleted(taskId);

        verify(repository).existsById(taskId);
        verify(repository).deleteById(taskId);

    }

    @Test
    void deleted_shouldThrow_whenNotFound(){
        when(repository.existsById(taskId)).thenReturn(false);

        assertThrows(RuntimeException.class,
                ()->testService.deleted(taskId));

        verify(repository).existsById(taskId);
        verify(repository, never()).deleteById(taskId);
    }

    @Test
    void updateStatus_shouldWork(){
        TaskStatus newStatus = TaskStatus.IN_PROGRESS;

        Task taskStatus = new Task();
        taskStatus.setId(taskId);
        taskStatus.setStatus(TaskStatus.NEW);

        Task taskStatusUpdate = new Task();
        taskStatusUpdate.setId(taskId);
        taskStatusUpdate.setStatus(newStatus);

        when(repository.findById(taskId)).thenReturn(Optional.of(taskStatus));
        when(repository.save(taskStatus)).thenReturn(taskStatusUpdate);

        Task actual = testService.updateStatus(taskId, newStatus);

        assertNotNull(actual);
        assertEquals(newStatus, actual.getStatus());
        assertEquals(taskId, actual.getId());

        verify(repository).findById(taskId);
        verify(repository).save(taskStatus);
    }

    @Test
    void updateStatus_shouldThrow_whenTaskNotFound() {
        TaskStatus newStatus = TaskStatus.DONE;

        when(repository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> testService.updateStatus(taskId, newStatus));

        verify(repository).findById(taskId);
        verify(repository, never()).save(any());
    }

    @Test
    void updatePriority_shouldWork(){
        TaskPriority priority = TaskPriority.HIGH;

        Task taskPriority = new Task();
        taskPriority.setId(taskId);
        taskPriority.setPriority(TaskPriority.LOW);

        Task taskPriorityUpdate = new Task();
        taskPriorityUpdate.setId(taskId);
        taskPriorityUpdate.setPriority(priority);

        when(repository.findById(taskId)).thenReturn(Optional.of(taskPriority));
        when(repository.save(taskPriority)).thenReturn(taskPriorityUpdate);

        Task actual = testService.updatePriority(taskId, priority);

        assertNotNull(actual);
        assertEquals(priority, actual.getPriority());
        assertEquals(taskId, actual.getId());

        verify(repository).findById(taskId);
        verify(repository).save(taskPriority);
    }

    @Test
    void updatePriority_shouldThrow_whenTaskNotFound(){
        TaskPriority newPriority = TaskPriority.HIGH;

        when(repository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                ()->testService.updatePriority(taskId, newPriority));

        verify(repository).findById(taskId);
        verify(repository, never()).save(task);
    }
}
