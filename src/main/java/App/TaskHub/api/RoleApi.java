package App.TaskHub.api;

import App.TaskHub.dto.req.role.RoleRequest;
import App.TaskHub.dto.res.role.RoleResponse;
import App.TaskHub.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/role/v1")
public class RoleApi {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<RoleResponse> get(){
        return roleService.get();
    }

    @GetMapping("/{roleId}")
    public RoleResponse getById(@PathVariable("roleId")UUID id){
        return roleService.getById(id);
    }

    @PostMapping
    public ResponseEntity<RoleResponse> create(@RequestBody RoleRequest request){
        RoleResponse create = roleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    @PutMapping("/{roleId}")
    public RoleResponse update(@PathVariable("roleId") UUID id, @RequestBody RoleRequest request){
        return roleService.update(id, request);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> delete(@PathVariable("roleId") UUID id){
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

