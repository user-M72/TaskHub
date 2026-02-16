package app.TaskHub.api;

import app.TaskHub.dto.req.comment.CommentRequest;
import app.TaskHub.dto.res.comment.CommentResponse;
import app.TaskHub.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/comment/v1")
public class CommentApi {

    @Autowired
    private CommentService service;

    @GetMapping
    public List<CommentResponse> get(){
        return service.get();
    }

    @GetMapping("/{commentId}")
    public CommentResponse getById(@PathVariable("commentId")UUID id){
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> created(@RequestBody CommentRequest request){
        CommentResponse created = service.created(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{commentId}")
    public CommentResponse updated(@PathVariable("commentId") UUID id,
                                   @RequestBody CommentRequest request){

        return null;
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleted(@PathVariable("commentId") UUID id){
        service.deleted(id);
        return ResponseEntity.noContent().build();
    }
}
