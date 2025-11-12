package App.TaskHub.service.serviceImpl;

import App.TaskHub.dto.req.UserRequest;
import App.TaskHub.dto.res.UserResponse;
import App.TaskHub.entity.Role;
import App.TaskHub.entity.User;
import App.TaskHub.mapper.UserMapper;
import App.TaskHub.repository.UserRepository;
import App.TaskHub.service.RoleService;
import App.TaskHub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private UserMapper mapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> get() {

        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getById(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found by id: " + id));
        return mapper.toDto(user);
    }

    @Override
    public UserResponse create(UserRequest request) {
        Set<Role> roleList = roleService.getByIdList(request.roleIds());
        User user = mapper.toEntity(request, roleList, passwordEncoder.encode(request.password()));
        return mapper.toDto(repository.save(user));
    }

    @Override
    public UserResponse update(UUID id, UserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found by id: " + id));
        Set<Role> roles = roleService.getByIdList(request.roleIds());
        mapper.updateFromDto(request, roles, user);
        return mapper.toDto(repository.save(user));
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)){
            throw new RuntimeException("User not found by id; " + id);
        }
        repository.deleteById(id);
    }
}
