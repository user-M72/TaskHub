package App.TaskHub.api;

import App.TaskHub.dto.res.comment.CommentResponse;
import App.TaskHub.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/comment/v1")
public class CommentApi {

    @Autowired
    private CommentService service;

    @GetMapping
    public List<CommentResponse> get(){
        return service.get();
    }
}
