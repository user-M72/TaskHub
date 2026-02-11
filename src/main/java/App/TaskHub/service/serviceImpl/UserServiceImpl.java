package App.TaskHub.service.serviceImpl;

import App.TaskHub.dto.req.LoginRequest;
import App.TaskHub.dto.req.ProfileUpdateRequest;
import App.TaskHub.dto.req.user.UserRequest;
import App.TaskHub.dto.res.login.LoginResponse;
import App.TaskHub.dto.res.user.UserResponse;
import App.TaskHub.entity.Role;
import App.TaskHub.entity.User;
import App.TaskHub.entity.enums.Roles;
import App.TaskHub.mapper.UserMapper;
import App.TaskHub.repository.RoleRepository;
import App.TaskHub.repository.UserRepository;
import App.TaskHub.service.RoleService;
import App.TaskHub.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final String errorLogin = "Invalid username or password";

    @Autowired
    private UserRepository repository;
    @Autowired
    private UserMapper mapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

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


        if (updateRequest.username() != null && !updateRequest.username().isEmpty()) {

            if (!user.getUsername().equals(updateRequest.username())) {
                if (repository.existsByUsername(updateRequest.username())) {
                    throw new RuntimeException("Username already exists");
                }
                user.setUsername(updateRequest.username());
            }
        }


        if (updateRequest.email() != null && !updateRequest.email().isEmpty()) {
            if (!user.getEmail().equals(updateRequest.email())) {
                if (repository.existsByEmail(updateRequest.email())) {
                    throw new RuntimeException("Email already exists");
                }
                user.setEmail(updateRequest.email());
            }
        }


        if (updateRequest.currentPassword() != null && updateRequest.newPassword() != null) {

            if (!passwordEncoder.matches(updateRequest.currentPassword(), user.getPassword())) {
                throw new RuntimeException("Current password is incorrect");
            }


            if (updateRequest.newPassword().length() < 6) {
                throw new RuntimeException("New password must be at least 6 characters long");
            }


            user.setPassword(passwordEncoder.encode(updateRequest.newPassword()));
        }


        User updatedUser = repository.save(user);


        return mapper.toDto(updatedUser);
    }
}
