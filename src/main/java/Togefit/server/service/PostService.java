package Togefit.server.service;

import Togefit.server.domain.Post.Comment;
import Togefit.server.domain.Post.Post;
import Togefit.server.domain.Post.PostImage;
import Togefit.server.domain.Post.Tag;
import Togefit.server.model.Post.CommentInfo;
import Togefit.server.model.Post.PostInfo;
import Togefit.server.repository.Post.CommentRepository;
import Togefit.server.repository.Post.PostImageRepository;
import Togefit.server.repository.Post.PostRepository;
import Togefit.server.repository.Post.TagRepository;
import Togefit.server.response.error.CustomException;
import Togefit.server.response.error.Error;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostImageRepository postImageRepository;
    private final CommentRepository commentRepository;
    private final AwsS3Service awsS3Service;

    public PostService(PostRepository postRepository, TagRepository tagRepository, PostImageRepository postImageRepository, CommentRepository commentRepository, AwsS3Service awsS3Service) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postImageRepository = postImageRepository;
        this.commentRepository = commentRepository;
        this.awsS3Service = awsS3Service;
    }

    public void addPost(Post post, String tag_list, List<MultipartFile> multipartFiles) throws IOException {
        // post 저장 -> postId로 (not null) tag_list 저장 (콤마 기준으로 분리하기), (not null) multipartFile 저장..

        postRepository.save(post);

        Long postId = post.getId();

        if(tag_list != null){
            saveTag(tag_list,postId);
        }

        if(multipartFiles != null){
            saveImageFiles(postId, multipartFiles);
        }
    }

    private void saveTag(String tag_list, Long postId){
        String[] tagList = getTagList(tag_list);
        for(String t : tagList){
            Tag tag = new Tag(postId, t);
            tagRepository.save(tag);
        }
    }

    public void saveImageFiles(Long postId, List<MultipartFile> multipartFiles) throws IOException {
        List<String> imageFiles = this.awsS3Service.multipleUploadFile(multipartFiles);
        for(String i : imageFiles){
            PostImage postImage = new PostImage(postId, i);
            postImageRepository.save(postImage);
        }
    }

    private String[] getTagList(String tags){
        return tags.split(",");
    }

    @Transactional
    public void deletePost(String userId, Long postId){
        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            throw new CustomException(new Error("해당 글을 찾지 못했습니다."));
        }

        if(!findPost.get().getUserId().equals(userId)){
            throw new CustomException(new Error(403, "작성자만 삭제할 수 있습니다."));
        }

        postRepository.delete(findPost.get());
        tagRepository.deleteByPostId(postId);
        postImageRepository.deleteByPostId(postId);
    }

    @Transactional
    public void updatePost(PostInfo postInfo, String tag_list, Long postId, String userId, List<MultipartFile> multipartFiles) throws IOException {
        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            throw new CustomException(new Error("해당 글을 찾지 못했습니다."));
        }

        if(!findPost.get().getUserId().equals(userId)){
            throw new CustomException(new Error(403, "작성자만 삭제할 수 있습니다."));
        }

        Post newPost = updatePost(findPost.get(), postInfo);
        postRepository.save(newPost);

        if(tag_list != null){
            tagRepository.deleteByPostId(postId);
            saveTag(tag_list, postId);
        }

        if(multipartFiles != null){
            saveImageFiles(postId, multipartFiles);
        }
    }

    private Post updatePost(Post post, PostInfo postInfo){
        if(postInfo.getContents() != null){
            post.setContents(postInfo.getContents());
        }
        if(postInfo.getMeal() != null){
            post.setMeal(postInfo.getMeal());
        }
        if(postInfo.getRoutine() != null){
            post.setRoutine(postInfo.getRoutine());
        }
        post.setIs_open(postInfo.getIs_open());

        return post;
    }

    public void addComment(CommentInfo commentInfo, String userId, String nickname){
        Long postId = commentInfo.getPostId();
        String content = commentInfo.getContent();

        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            throw new CustomException(new Error("해당 글을 찾지 못했습니다."));
        }

        Comment comment = new Comment(postId, nickname, userId, content);
        commentRepository.save(comment);
    }
}
