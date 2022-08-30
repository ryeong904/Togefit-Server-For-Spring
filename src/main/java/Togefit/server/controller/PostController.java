package Togefit.server.controller;

import Togefit.server.model.IdInfo;
import Togefit.server.model.PostInfo;
import Togefit.server.response.OperationResponse;
import Togefit.server.service.PostService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RequestMapping("/posts")
@RestController
public class PostController {

    private PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/")
    public OperationResponse register(@RequestParam String contents,
                                      @RequestParam String tag_list,
                                      @RequestParam Long meal,
                                      @RequestParam Long routine,
                                      @RequestParam(required = false) Boolean is_open,
                                      @RequestPart(value = "images") List<MultipartFile> multipartFiles,
                                      HttpServletRequest request) throws IOException {
        OperationResponse resp = new OperationResponse();
        String userId = (String) request.getAttribute("userId");
        String nickname = (String) request.getAttribute("nickname");

        if(is_open == null){
            is_open = true;
        }

        PostInfo post = new PostInfo(userId, nickname, contents, is_open, meal, routine);

        postService.addPost(post, tag_list, multipartFiles);


        resp.setResult("등록되었습니다.");
        return resp;
    }

    @DeleteMapping
    public OperationResponse delete(@RequestBody IdInfo IdInfo, HttpServletRequest request){
        OperationResponse resp = new OperationResponse();
        String userId = (String) request.getAttribute("userId");

        Long postId = IdInfo.getId();

        postService.deletePost(userId, postId);

        resp.setResult("삭제되었습니다.");
        return resp;
    }

}
