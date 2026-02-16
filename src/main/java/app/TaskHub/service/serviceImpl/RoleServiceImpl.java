package app.TaskHub.service.serviceImpl;

import app.TaskHub.dto.req.role.RoleRequest;
import app.TaskHub.dto.res.role.RoleResponse;
import app.TaskHub.entity.Role;
import app.TaskHub.entity.enums.Roles;
import app.TaskHub.mapper.RoleMapper;
import app.TaskHub.repository.RoleRepository;
import app.TaskHub.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    @Override
    public List<RoleResponse> get() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse getById(UUID id) {
        Role role =repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Role not found by id:" + id));
        return mapper.toDto(role);
    }

    @Override
    public RoleResponse create(RoleRequest request) {
        Role role = mapper.toEntity(request);
        return mapper.toDto(repository.save(role));
    }

    @Override
    public RoleResponse update(UUID id, RoleRequest request) {
        Role role = repository.findById(id).orElseThrow(()-> new RuntimeException("Role not found by id: " + id));
        mapper.updateFromDto(request, role);
        return mapper.toDto(repository.save(role));
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)){
            throw new RuntimeException("Role not found by id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public Set<Role> getByIdList(List<UUID> uuids) {
        return new HashSet<>(repository.findAllById(uuids));
    }

    @Override
    public Optional<Role> getByName(String name) {
        return repository.findByName(Roles.valueOf(name));
    }
}
