package App.TaskHub.entity;


import App.TaskHub.entity.BaseDomain.BaseDomain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseDomain<UUID>{

    private String firstName;
    private String lastName;

    private String username;
    private String password;

    private String phoneNumber;
    private String email;


    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
    private Set<Role> roles;

}