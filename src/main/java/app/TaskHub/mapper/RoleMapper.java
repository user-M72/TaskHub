package app.TaskHub.mapper;

import app.TaskHub.dto.req.role.RoleRequest;
import app.TaskHub.dto.res.role.RoleResponse;
import app.TaskHub.entity.Role;
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
