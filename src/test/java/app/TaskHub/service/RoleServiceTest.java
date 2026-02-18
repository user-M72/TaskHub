package app.TaskHub.service;

import app.TaskHub.dto.req.role.RoleRequest;
import app.TaskHub.dto.res.role.RoleResponse;
import app.TaskHub.entity.Role;
import app.TaskHub.entity.enums.Roles;
import app.TaskHub.mapper.RoleMapper;
import app.TaskHub.repository.RoleRepository;
import app.TaskHub.service.serviceImpl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @InjectMocks
    RoleServiceImpl testService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    private final UUID roleId = UUID.randomUUID();
    private RoleResponse roleResponse;
    private RoleRequest request;
    private Role role;

    @BeforeEach
    void setup(){

         roleResponse = new RoleResponse(
                roleId,
                "",
                ""
        );

        request = new RoleRequest(
                "",
                ""

        );
        role = new Role();
    }
    @Test
    void get_shouldWork() {
        List<Role> roles = List.of(role);

        RoleResponse response = new RoleResponse(
                roleId,
                "",
                ""
        );

        List<RoleResponse> expect = List.of(response);

        when(roleRepository.findAll()).thenReturn(roles);
        when(roleMapper.toDto(role)).thenReturn(response);

        List<RoleResponse> actual = testService.get();

        assertEquals(expect, actual);

        verify(roleRepository).findAll();
        verify(roleMapper).toDto(role);
    }

    @Test
    void getById_shouldWork() {

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleResponse);

        RoleResponse actual = testService.getById(roleId);

        assertEquals(roleResponse, actual);

        verify(roleRepository).findById(roleId);
        verify(roleMapper).toDto(role);

    }

    @Test
    void create_shouldWork() {

        when(roleMapper.toEntity(request)).thenReturn(role);
        when(roleRepository.save(role)).thenAnswer(i -> i.getArgument(0));
        when(roleMapper.toDto(role)).thenReturn(roleResponse);

        RoleResponse actual = testService.create(request);

        assertEquals(roleResponse, actual);

        verify(roleMapper).toEntity(request);
        verify(roleRepository).save(role);
        verify(roleMapper).toDto(role);
    }

    @Test
    void update_shouldWork() {

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.toDto(role)).thenReturn(roleResponse);

        RoleResponse actual = testService.update(roleId, request);

        assertEquals(roleResponse, actual);

        verify(roleRepository).findById(roleId);
        verify(roleMapper).updateFromDto(request, role);
        verify(roleRepository).save(role);
        verify(roleMapper).toDto(role);
    }

    @Test
    void delete_shouldWork() {

        when(roleRepository.existsById(roleId)).thenReturn(true);

        testService.delete(roleId);

        verify(roleRepository).existsById(roleId);
        verify(roleRepository).deleteById(roleId);
    }

    @Test
    void delete_shouldThrow_whenNotFound(){

        when(roleRepository.existsById(roleId)).thenReturn(false);

        assertThrows(RuntimeException.class,
                ()-> testService.delete(roleId));

        verify(roleRepository).existsById(roleId);
    }

    @Test
    void getByIdList_shouldWork(){
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        List<UUID> ids = List.of(id1, id2);

        Role role1 = new Role();
        Role role2 = new Role();
        List<Role> roles = List.of(role1, role2);

        when(roleRepository.findAllById(ids)).thenReturn(roles);

        Set<Role> actual = testService.getByIdList(ids);

        assertEquals(new HashSet<>(roles), actual);

        verify(roleRepository).findAllById(ids);
    }

    @Test
    void getByName(){
        role.setName(Roles.USER);

        when(roleRepository.findByName(Roles.USER)).thenReturn(Optional.of(role));

        Optional<Role> actual = testService.getByName(Roles.USER.name());

        assertTrue(actual.isPresent());
        assertEquals(role, actual.get());

        verify(roleRepository).findByName(Roles.USER);
    }
}
