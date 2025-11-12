package App.TaskHub.entity;

import App.TaskHub.entity.BaseDomain.BaseDomain;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseDomain<UUID> {

    private String name;

    private String description;

}
