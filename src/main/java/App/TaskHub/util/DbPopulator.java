//package App.TaskHub.util;
//
//import App.TaskHub.dto.req.role.RoleRequest;
//import App.TaskHub.dto.req.user.UserRequest;
//import App.TaskHub.dto.res.role.RoleResponse;
//import App.TaskHub.dto.res.user.UserResponse;
//import App.TaskHub.entity.Role;
//import App.TaskHub.entity.enums.Roles;
//import App.TaskHub.service.RoleService;
//import App.TaskHub.service.UserService;
//import lombok.AllArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Optional;
//
//@Component
//@AllArgsConstructor
//public class DbPopulator implements CommandLineRunner {
//
//    private final UserService userService;
//    private final RoleService roleService;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        Role adminRole = createdRoleAdmin();
//        List<UserResponse> users = userService.get();
//        if (users.isEmpty()){
//            userService.create(
//                    new UserRequest(
//                            "Admin",
//                            "Admin",
//                            "admin",
//                            "admin",
//                            "00000",
//                            "admin@gmail.com",
//                            List.of(adminRole.getId())));
//        }
//    }
//
//    public Role createdRoleAdmin(){
//        Optional<Role> admin = roleService.getByName("ADMIN");
//        if (admin.isPresent()){
//            return admin.get();
//        } else {
//            RoleResponse adminRole =roleService.create(new RoleRequest("ADMIN", "Administrator"));
//
//            Role role = new Role();
//            role.setId(adminRole.id());
//            role.setName(Roles.valueOf(adminRole.name()));
//            role.setDescription(adminRole.description());
//
//            return role;
//        }
//
//    }
//}
