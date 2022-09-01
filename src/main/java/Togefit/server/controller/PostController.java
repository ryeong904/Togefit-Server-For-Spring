package Togefit.server.controller;

import Togefit.server.domain.Post.Post;
import Togefit.server.model.IdInfo;
import Togefit.server.model.Post.ArticleInfo;
import Togefit.server.model.Post.CommentInfo;
import Togefit.server.model.Post.PostInfo;
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

    private final PostService postService;
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

        Post post = new Post(userId, nickname, contents, is_open, meal, routine);

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

    @PatchMapping("/{postId}")
    public OperationResponse update(@PathVariable Long postId,
                                    @ModelAttribute PostInfo postInfo,
                                    @RequestParam(required = false) String tag_list,
                                    @RequestPart(required = false, value = "images") List<MultipartFile> multipartFiles,
                                    HttpServletRequest request) throws IOException {
        OperationResponse resp = new OperationResponse();
        String userId = (String) request.getAttribute("userId");

        postService.updatePost(postInfo, tag_list, postId, userId, multipartFiles);

        resp.setResult("수정되었습니다.");
        return resp;
    }

    @PostMapping("/comment")
    public OperationResponse commentRegister(@RequestBody CommentInfo commentInfo,
                                             HttpServletRequest request){
        OperationResponse resp = new OperationResponse();
        String userId = (String) request.getAttribute("userId");
        String nickname = (String) request.getAttribute("nickname");


        postService.addComment(commentInfo, userId, nickname);
        resp.setResult("댓글이 등록되었습니다.");
        return resp;
    }

    @DeleteMapping("/comment")
    public OperationResponse commentDelete(@RequestBody CommentInfo commentInfo,
                                           HttpServletRequest request){
        OperationResponse resp = new OperationResponse();
        String userId = (String) request.getAttribute("userId");

        postService.deleteComment(userId, commentInfo.getCommentId());

        resp.setResult("댓글이 삭제되었습니다.");
        return resp;
    }

    @PatchMapping("/comment")
    public OperationResponse commentUpdate(@RequestBody CommentInfo commentInfo,
                                           HttpServletRequest request){
        OperationResponse resp = new OperationResponse();
        String userId = (String) request.getAttribute("userId");

        postService.updateComment(commentInfo, userId);

        resp.setResult("댓글이 수정되었습니다.");
        return resp;
    }

    @GetMapping("/{postId}")
    public ArticleInfo getPost(@PathVariable Long postId){
        return postService.getPostById(postId);
    }

    @GetMapping("/all")
    public List<ArticleInfo> getAllPosts(@RequestParam("limit") int limit,
                                @RequestParam("reqNumber") int reqNumber){
        return postService.getAllPost(limit, reqNumber);
    }

    @GetMapping("/user")
    public List<ArticleInfo> getPostListByDate(@RequestParam("userId") String userId,
                                                    @RequestParam("year") int year,
                                                    @RequestParam("month") int month,
                                                    @RequestParam("limit") int limit,
                                                    @RequestParam("reqNumber") int reqNumber){
        return postService.getPostListByDate(userId, year, month, limit, reqNumber);

    }

}