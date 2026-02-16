package app.TaskHub.service.serviceImpl;

import app.TaskHub.dto.req.LoginRequest;
import app.TaskHub.dto.req.ProfileUpdateRequest;
import app.TaskHub.dto.req.user.UserRequest;
import app.TaskHub.dto.res.login.LoginResponse;
import app.TaskHub.dto.res.user.UserResponse;
import app.TaskHub.entity.Role;
import app.TaskHub.entity.User;
import app.TaskHub.entity.enums.Roles;
import app.TaskHub.mapper.UserMapper;
import app.TaskHub.repository.RoleRepository;
import app.TaskHub.repository.UserRepository;
import app.TaskHub.service.RoleService;
import app.TaskHub.service.UserService;
import app.TaskHub.util.UpdateUserValidator;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final String errorLogin = "Invalid username or password";

    private final UserRepository repository;
    private final UserMapper mapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UpdateUserValidator updateUserValidator;

    @Transactional(readOnly = true)
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
        Set<Role> roleSet = roleService.getByIdList(request.roleIds());
        String encodePassword = passwordEncoder.encode(request.password());
        User user = mapper.toEntity(request, roleSet, encodePassword);
        User save = repository.save(user);
        return mapper.toDto(save);
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

    @Override
    public UserResponse register(UserRequest request) {
        if (repository.existsByUsername(request.username())){
            throw new RuntimeException("Username already taken");
        }

        Role userRole = roleRepository.findByName(Roles.USER).orElseThrow(()-> new RuntimeException("Role User is not"));
        Set<Role> roles = Set.of(userRole);

        User user = mapper.toEntity(request, roles, passwordEncoder.encode(request.password()));

        return mapper.toDto(repository.save(user));
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = repository.findByUsername(request.username())
                .orElseThrow(()-> new RuntimeException(errorLogin));

        if (!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new RuntimeException(errorLogin);
        }

        return mapper.toLoginDto(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UUID id, ProfileUpdateRequest updateRequest) {

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found by id: " + id));


        updateUserValidator.validateAndSetUsername(user, updateRequest.username());
        updateUserValidator.validateAndSetEmail(user, updateRequest.email());
        updateUserValidator.validateAndSetPassword(user, updateRequest.currentPassword(), updateRequest.newPassword());

        User updatedUser = repository.save(user);


        return mapper.toDto(updatedUser);
    }
}
