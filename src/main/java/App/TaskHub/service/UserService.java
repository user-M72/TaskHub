package App.TaskHub.service;

import App.TaskHub.dto.req.LoginRequest;
import App.TaskHub.dto.req.user.UserRequest;
import App.TaskHub.dto.res.login.LoginResponse;
import App.TaskHub.dto.res.user.UserResponse;

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
}
