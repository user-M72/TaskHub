package App.TaskHub.repository;

import App.TaskHub.entity.Task;
import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findAllByCreatorId(UUID creatorId, Pageable pageable);

    Page<Task> findAllByAssigneeId(UUID assigneeId, Pageable pageable);
}
