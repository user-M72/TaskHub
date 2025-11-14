package App.TaskHub.mapper;

import App.TaskHub.dto.req.user.UserRequest;
import App.TaskHub.dto.res.user.UserResponse;
import App.TaskHub.entity.Role;
import App.TaskHub.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse toDto(User user);

    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "password", source = "encodedPassword")
    User toEntity(UserRequest request, Set<Role> roles,  String encodedPassword);


    @Mapping(target = "roles", source = "roles")
    void updateFromDto(UserRequest request, Set<Role> roles, @MappingTarget User user);
}
