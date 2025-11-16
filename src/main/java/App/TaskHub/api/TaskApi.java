package App.TaskHub.api;

import App.TaskHub.dto.req.task.TaskRequest;
import App.TaskHub.dto.res.task.TaskResponse;
import App.TaskHub.entity.enums.TaskPriority;
import App.TaskHub.entity.enums.TaskStatus;
import App.TaskHub.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/task/v1")
public class TaskApi {

    @Autowired
    private TaskService service;

    @GetMapping
    public List<TaskResponse> get(){
        return service.get();

    }

    @GetMapping("/{taskId}")
    public TaskResponse getById(@PathVariable("taskId")UUID id){
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest request){
        TaskResponse create = service.created(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    @PutMapping("/{taskId}")
    public TaskResponse updated(@PathVariable("taskId") UUID id,
                                @RequestBody TaskRequest request){
        return null;
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleted(@PathVariable("taskId") UUID id){
        service.deleted(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statues")
    public TaskStatus[] getStatuses(){
        return TaskStatus.values();
    }

    @GetMapping("/priorities")
    public TaskPriority[] getPriorities(){
        return TaskPriority.values();
    }

}
