package app.TaskHub.util;

import app.TaskHub.dto.req.user.UserRequest;
import app.TaskHub.dto.res.user.UserResponse;
import app.TaskHub.entity.Role;
import app.TaskHub.service.RoleService;
import app.TaskHub.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbPopulator implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    public DbPopulator(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) {

        List<UserResponse> users = userService.get();

        if (users.isEmpty()) {

            Role admin = roleService.getByName("ADMIN").orElseThrow(()-> new RuntimeException("Admin role no found"));

            userService.create(
                    new UserRequest(
                            "Admin",
                            "Admin",
                            "user",
                            "user",
                            "00000",
                            "admin@gmail.com",
                            List.of(admin.getId())
                    )
            );
        }
    }
}
