package app.TaskHub.service;

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
import app.TaskHub.service.serviceImpl.UserServiceImpl;
import app.TaskHub.util.UpdateUserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks UserServiceImpl testedService;

    @Mock private UserRepository repository;
    @Mock private UserMapper mapper;
    @Mock private RoleService roleService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private RoleRepository roleRepository;
    @Mock private UpdateUserValidator validator;

    @Test
    void get_shouldWork() {
        User user = new User();
        List<User> users = List.of(user);

        UserResponse userResponse = new UserResponse(
                UUID.randomUUID(),
                "",
                "",
                "",
                "",
                "",
                "",
                Set.of()
        );
        List<UserResponse> expected = List.of(userResponse);

        when(repository.findAll()).thenReturn(users);
        when(mapper.toDto(user)).thenReturn(userResponse);

        List<UserResponse> actual = testedService.get();

        assertEquals(expected, actual);

        verify(repository).findAll();
        verify(mapper, times(users.size())).toDto(any(User.class));
    }

    @Test
    void getById_shouldWork() {
//        UUID id = UUID.randomUUID();
//
//        User user = new User();
//        user.setId(id);
//
//        UserResponse response = new UserResponse(
//                id,
//                "John",
//                "Doe",
//                "joe",
//                "pass",
//                "123456",
//                "test@mail.com",
//                Set.of()
//        );
//
//        when(repository.findById(id)).thenReturn(Optional.of(user));
//        when(mapper.toDto(user)).thenReturn(response);
//
//        UserResponse result = testedService.getById(id);
//
//        assertNotNull(result);
//        assertEquals(id, result.id());
//
//        verify(repository).findById(id);
//        verify(mapper).toDto(user);

        UUID id = UUID.randomUUID();

        User user = new User();
        user.setId(id);

        UserResponse response = new UserResponse(
                id,
                "",
                "",
                "",
                "",
                "",
                "",
                Set.of()
        );

        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(mapper.toDto(user)).thenReturn(response);

        UserResponse result = testedService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.id());

        verify(repository).findById(id);
        verify(mapper).toDto(user);
    }

    @Test
    void getById_shouldThrowException_whenUserNotFound() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                ()->testedService.getById(id)
        );

        assertTrue(exception.getMessage().contains("User not found"));

        verify(repository).findById(id);
    }

    @Test
    void create_shouldWork() {
        UUID roleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String encodedPassword = "encoded";

        Role role = new Role();
        role.setId(roleId);
        role.setName(Roles.USER);

        Set<Role> roleSet = Set.of(role);

        UserRequest request = new UserRequest(
                "firstname",
                "lastname",
                "USERNAME",
                "password",
                "phone",
                "",
                List.of(roleId)
        );

        UserResponse expected = new UserResponse(
                userId,
                request.firstName(),
                request.lastName(),
                request.username(),
                encodedPassword,
                request.phoneNumber(),
                request.email(),
                roleSet
        );

        User user = new User();
        user.setId(userId);
        user.setCreatedBy(userId);
        user.setCreatedDate(Instant.now());
        user.setUpdatedBy(userId);
        user.setUpdatedDate(Instant.now());

        when(roleService.getByIdList(request.roleIds()))
                .thenReturn(roleSet);
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(mapper.toEntity(request, roleSet, encodedPassword)).thenReturn(user);
        when(repository.save(user)).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDto(user)).thenReturn(expected);

        UserResponse actual = testedService.create(request);

        assertEquals(expected, actual);

        verify(roleService).getByIdList(request.roleIds());
        verify(passwordEncoder).encode(request.password());
        verify(mapper).toEntity(request, roleSet, encodedPassword);
        verify(repository).save(user);
        verify(mapper).toDto(user);
    }

    @Test
    void update_ShouldWork(){
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        Role role = new Role();
        role.setId(roleId);
        role.setName(Roles.USER);

        Set<Role> roleSet = Set.of(role);

        UserRequest request = new UserRequest(
                "",
                "",
                "",
                "",
                "",
                "",
                List.of(roleId)
        );

        User user = new User();

        UserResponse response = new UserResponse(
                userId,
                "",
                "",
                "",
                "",
                "",
                "",
                roleSet

        );

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(roleService.getByIdList(request.roleIds())).thenReturn(roleSet);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(response);

        UserResponse result = testedService.update(userId, request);

        assertNotNull(result);

        verify(repository).findById(userId);
        verify(roleService).getByIdList(request.roleIds());
        verify(mapper).updateFromDto(request, roleSet, user);
        verify(repository).save(user);
        verify(mapper).toDto(user);
    }

    @Test
    void delete_shouldWord() {
        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(true);

        testedService.delete(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> testedService.delete(id));

        verify(repository).existsById(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void register_shouldWord(){
        UUID userId = UUID.randomUUID();

        String encodePassword = "encode";

        User user = new User();
        user.setId(userId);

        Role role = new Role();
        role.setName(Roles.USER);

        Set<Role> roleSet = Set.of(role);

        UserRequest request = new UserRequest(
                "",
                "",
                "",
                "",
                "",
                "",
                List.of()
        );
        UserResponse response = new UserResponse(
                userId,
                "",
                "",
                "",
                "",
                "",
                "",
                roleSet
        );

        when(repository.existsByUsername(request.username())).thenReturn(false);
        when(roleRepository.findByName(Roles.USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(request.password())).thenReturn(encodePassword);
        when(mapper.toEntity(request, roleSet, encodePassword)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(response);

        UserResponse result = testedService.register(request);

        assertNotNull(result);

        verify(repository).existsByUsername(request.username());
        verify(roleRepository).findByName(Roles.USER);
        verify(passwordEncoder).encode(request.password());
        verify(repository).save(user);
        verify(mapper).toDto(user);
    }

    @Test
    void register_shouldThrow_whenUsernameExists(){
        UserRequest request = new UserRequest(
                "",
                "",
                "username",
                "",
                "",
                "",
                List.of()
        );

        when(repository.existsByUsername(request.username())).thenReturn(true);

        assertThrows(RuntimeException.class,
                ()-> testedService.register(request));

        verify(repository).existsByUsername(request.username());
        verify(repository, never()).save(any());

    }

    @Test
    void login_shouldWork() {
        String username = "username";
        String password = "password";
        LoginRequest request = new LoginRequest(username, password);

        Role role = new Role();
        role.setName(Roles.USER);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(username);
        user.setEmail("email");
        user.setRoles(Set.of(role));


        LoginResponse expected = new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(r -> r.getName().toString()).collect(Collectors.toSet())
        );

        when(repository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(mapper.toLoginDto(user)).thenReturn(expected);

        LoginResponse actual = testedService.login(request);

        assertEquals(expected, actual);

        verify(repository).findByUsername(username);
        verify(passwordEncoder).matches(request.password(), user.getPassword());
        verify(mapper).toLoginDto(user);
    }


    @Test
    void login_shouldThrow_whenUserNotFound() {
        String username = "username";
        String password = "password";
        LoginRequest request = new LoginRequest(username, password);

        when(repository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> testedService.login(request));

        verify(repository).findByUsername(username);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(mapper, never()).toLoginDto(any());
    }

    @Test
    void login_shouldThrowException_whenPasswordWrong() {
        String username = "username";
        String password = "password";

        LoginRequest request = new LoginRequest(username, password);
        User user = new User();
        user.setPassword("encoded"); // важно задать пароль

        when(repository.findByUsername(username))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword()))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> testedService.login(request));

        verify(passwordEncoder).matches(password, "encoded");
        verify(mapper, never()).toLoginDto(any());
    }

    @Test
    void updateProfile_shouldWord(){
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Role role = new Role();

        Set<Role> roleSet = Set.of(role);

        UserResponse response = new UserResponse(
                userId,
                "",
                "",
                "",
                "",
                "",
                "",
                roleSet
        );

        ProfileUpdateRequest request = new ProfileUpdateRequest(
                "",
                "",
                "",
                ""
        );

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(response);

        UserResponse result = testedService.updateProfile(userId, request);
        assertNotNull(result);

        verify(repository).findById(userId);
        verify(validator).validateAndSetUsername(user, request.username());
        verify(validator).validateAndSetEmail(user, request.email());
        verify(validator).validateAndSetPassword(user, request.currentPassword(), request.newPassword());
        verify(repository).save(user);
        verify(mapper).toDto(user);

    }
}
