package App.TaskHub.entity;

import App.TaskHub.entity.BaseDomain.BaseDomain;
import App.TaskHub.entity.enums.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseDomain<UUID> {

    @Enumerated(EnumType.STRING)
    private Roles name;

    private String description;

}
