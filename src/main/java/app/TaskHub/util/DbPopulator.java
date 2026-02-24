package app.TaskHub.util;

import app.TaskHub.dto.req.user.UserRequest;
import app.TaskHub.dto.res.user.UserResponse;
import app.TaskHub.entity.Role;
import app.TaskHub.entity.enums.Roles;
import app.TaskHub.repository.RoleRepository;
import app.TaskHub.service.RoleService;
import app.TaskHub.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DbPopulator implements CommandLineRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        createRoleIfNotExits(Roles.ADMIN);
        createRoleIfNotExits(Roles.USER);
        createRoleIfNotExits(Roles.DIRECTOR);
        createRoleIfNotExits(Roles.MANAGER);

        List<UserResponse> users = userService.get();

        if (users.isEmpty()) {

            Role admin = roleRepository.findByName(Roles.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            Role userRole = roleRepository.findByName(Roles.USER)
                    .orElseThrow(() -> new RuntimeException("User role not found"));

            userService.create(
                    new UserRequest(
                            "Admin",
                            "Admin",
                            "admin",
                            "admin",
                            "00000",
                            "admin@gmail.com",
                            List.of(admin.getId())
                    )
            );

            userService.create(
                    new UserRequest(
                            "User", "User", "user", "user",  // ← добавь это
                            "11111", "user@gmail.com",
                            List.of(userRole.getId())
                    )
            );
        }
    }

    private void createRoleIfNotExits(Roles roleName){
        if (roleRepository.findByName(roleName).isEmpty()){
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
