package App.TaskHub.service;

import App.TaskHub.dto.req.UserRequest;
import App.TaskHub.dto.res.UserResponse;

import java.util.List;
import java.util.UUID;


public interface UserService {

    List<UserResponse> get();

    UserResponse getById(UUID id);

    UserResponse create(UserRequest request);

    UserResponse update(UUID id, UserRequest request);

    void delete(UUID id);
}
