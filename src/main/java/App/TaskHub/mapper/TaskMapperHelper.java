package App.TaskHub.mapper;

import App.TaskHub.entity.Task;
import App.TaskHub.repository.TaskRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TaskMapperHelper {

    @Autowired
    private TaskRepository taskRepository;

    public Task toEntity(UUID taskId) {
        if (taskId == null) {
            return null;
        }
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found by id: " + taskId));
    }
}
