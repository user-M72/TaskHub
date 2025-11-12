package App.TaskHub.api;

import App.TaskHub.dto.req.UserRequest;
import App.TaskHub.dto.res.UserResponse;
import App.TaskHub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users/v1")
public class UserApi {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserResponse> get(){
        return userService.get();
    }

    @GetMapping("/{userId}")
    public UserResponse getById(@PathVariable("userId")UUID id){
        return userService.getById(id);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request){
        UserResponse create = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    @PutMapping("/{userId}")
    public UserResponse update(@PathVariable("userId") UUID id, @RequestBody UserRequest request){
        return userService.update(id, request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") UUID id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
