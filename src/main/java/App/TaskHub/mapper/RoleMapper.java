package App.TaskHub.mapper;

import App.TaskHub.dto.req.role.RoleRequest;
import App.TaskHub.dto.res.role.RoleResponse;
import App.TaskHub.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    RoleResponse toDto(Role role);


    Role toEntity(RoleRequest request);

    @Mapping(target = "name", ignore = true)
    void updateFromDto(RoleRequest request, @MappingTarget Role role);
}
