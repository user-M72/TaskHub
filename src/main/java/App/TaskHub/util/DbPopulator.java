package App.TaskHub.util;

import App.TaskHub.dto.req.user.UserRequest;
import App.TaskHub.dto.res.user.UserResponse;
import App.TaskHub.entity.Role;
import App.TaskHub.service.RoleService;
import App.TaskHub.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DbPopulator implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    @Override
    public void run(String... args) {

        List<UserResponse> users = userService.get();

        if (users.isEmpty()) {

            Role admin = roleService.getByName("ADMIN").orElseThrow(()-> new RuntimeException("Admin role no found"));

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
        }
    }
}
