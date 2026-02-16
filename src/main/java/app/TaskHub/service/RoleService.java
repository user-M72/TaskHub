package app.TaskHub.service;

import app.TaskHub.dto.req.role.RoleRequest;
import app.TaskHub.dto.res.role.RoleResponse;
import app.TaskHub.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


public interface RoleService {

    List<RoleResponse> get();

    RoleResponse getById(UUID id);

    RoleResponse create(RoleRequest request);

    RoleResponse update(UUID id, RoleRequest request);

    void delete(UUID id);

    Set<Role> getByIdList(List<UUID> uuids);

    Optional<Role> getByName(String name);
}
