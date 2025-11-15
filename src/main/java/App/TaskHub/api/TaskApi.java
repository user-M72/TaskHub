package App.TaskHub.api;

import App.TaskHub.dto.res.task.TaskResponse;
import App.TaskHub.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/task/v1")
public class TaskApi {

    @Autowired
    private TaskService service;

    @GetMapping
    public List<TaskResponse> get(){
        return service.get();

    }
}
