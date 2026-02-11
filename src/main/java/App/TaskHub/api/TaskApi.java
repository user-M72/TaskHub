package App.TaskHub.api;

import App.TaskHub.dto.req.task.TaskRequest;
import App.TaskHub.dto.res.task.TaskResponse;
import App.TaskHub.entity.Task;
import App.TaskHub.entity.enums.TaskPriority;
import App.TaskHub.entity.enums.TaskStatus;
import App.TaskHub.service.TaskService;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/task/v1")
public class TaskApi {


    private final TaskService service;

    @GetMapping("/assignee/{id}")
    public Page<TaskResponse> getForAssignee(
            @PathVariable UUID id,
            @ParameterObject
            @PageableDefault(
                    sort = "createdDate",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable) {

        return service.getForAssignee(id, pageable);
    }

    @GetMapping("/creator/{id}")
    public Page<TaskResponse> getForCreator(
            @PathVariable UUID id,
            @ParameterObject
            @PageableDefault(
                    sort = "createdDate",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable) {
        return service.getForCreator(id, pageable);
    }

    @GetMapping("/{taskId}")
    public TaskResponse getById(@PathVariable("taskId") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest request) {
        TaskResponse create = service.created(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    @PutMapping("/{taskId}")
    public TaskResponse updated(@PathVariable("taskId") UUID id,
                                @RequestBody TaskRequest request) {
        return service.updated(id, request);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleted(@PathVariable("taskId") UUID id) {
        service.deleted(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statuses")
    public TaskStatus[] getStatuses() {
        return TaskStatus.values();
    }

    @GetMapping("/priorityStatuses")
    public TaskPriority[] getPriorities() {
        return TaskPriority.values();
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<?> updatedStatuses(@PathVariable("taskId") UUID id,
                                             @RequestBody Map<String, String> request) {
        String status = request.get("status");
        TaskStatus taskStatus = TaskStatus.valueOf(status);

        Task task = service.updateStatus(id, taskStatus);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{taskId}/priority")
    public ResponseEntity<?> updatedPriority(@PathVariable("taskId") UUID id,
                                             @RequestBody Map<String, String> request) {

        String priority = request.get("priority");
        TaskPriority taskPriority = TaskPriority.valueOf(priority);

        Task task = service.updatePriority(id, taskPriority);
        return ResponseEntity.ok(task);
    }

}
