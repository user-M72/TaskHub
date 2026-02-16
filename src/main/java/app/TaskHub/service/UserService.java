package app.TaskHub.service;

import app.TaskHub.dto.req.LoginRequest;
import app.TaskHub.dto.req.ProfileUpdateRequest;
import app.TaskHub.dto.req.user.UserRequest;
import app.TaskHub.dto.res.login.LoginResponse;
import app.TaskHub.dto.res.user.UserResponse;

import java.util.List;
import java.util.UUID;


public interface UserService {

    List<UserResponse> get();

    UserResponse getById(UUID id);

    UserResponse create(UserRequest request);

    UserResponse update(UUID id, UserRequest request);

    void delete(UUID id);

    UserResponse register(UserRequest request);

    LoginResponse login(LoginRequest request);

    UserResponse updateProfile(UUID id, ProfileUpdateRequest updateRequest);
}
