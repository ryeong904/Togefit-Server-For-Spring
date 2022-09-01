package Togefit.server.service;

import Togefit.server.domain.Post.Comment;
import Togefit.server.domain.Post.Post;
import Togefit.server.domain.Post.PostImage;
import Togefit.server.domain.Post.Tag;
import Togefit.server.model.Post.ArticleInfo;
import Togefit.server.model.Post.CommentInfo;
import Togefit.server.model.Post.PostInfo;
import Togefit.server.model.meal.MealInfo;
import Togefit.server.model.meal.MealInfoByArticleId;
import Togefit.server.repository.Post.CommentRepository;
import Togefit.server.repository.Post.PostImageRepository;
import Togefit.server.repository.Post.PostRepository;
import Togefit.server.repository.Post.TagRepository;
import Togefit.server.response.error.CustomException;
import Togefit.server.response.error.Error;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostImageRepository postImageRepository;
    private final CommentRepository commentRepository;
    private final AwsS3Service awsS3Service;
    private final MealService mealService;
    private final RoutineService routineService;

    public PostService(PostRepository postRepository, TagRepository tagRepository, PostImageRepository postImageRepository, CommentRepository commentRepository, AwsS3Service awsS3Service, MealService mealService, RoutineService routineService) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postImageRepository = postImageRepository;
        this.commentRepository = commentRepository;
        this.awsS3Service = awsS3Service;
        this.mealService = mealService;
        this.routineService = routineService;
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

    @Transactional
    public void deleteComment(String userId, Long commentId){
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()){
            throw new CustomException(new Error("해당 댓글을 찾지 못했습니다."));
        }

        if(!findComment.get().getUserId().equals(userId)){
            throw new CustomException(new Error("작성자만 삭제할 수 있습니다."));
        }

        commentRepository.deleteById(commentId);
    }

    public void updateComment(CommentInfo commentInfo, String userId){
        Optional<Comment> findComment = commentRepository.findById(commentInfo.getCommentId());
        if(findComment.isEmpty()){
            throw new CustomException(new Error("해당 댓글을 찾지 못했습니다."));
        }

        if(!findComment.get().getUserId().equals(userId)){
            throw new CustomException(new Error("작성자만 삭제할 수 있습니다."));
        }

        findComment.get().setContent(commentInfo.getContent());
        commentRepository.save(findComment.get());
    }

    public ArticleInfo getPostById(Long postId){
        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            throw new CustomException(new Error("해당 글을 찾지 못했습니다."));
        }
        return this.setArticle(findPost.get(), postId, findPost.get().getMeal(), findPost.get().getRoutine());
    }

    private ArticleInfo setArticle(Post post, Long postId, Long mealId, Long routineId){
        ArticleInfo article =
                new ArticleInfo(
                        post.getUserId(),
                        post.getNickname(),
                        post.getContents(),
                        post.getIs_open(),
                        post.getLikeCount()
                );

        article.setPost_image(this.getPostImageUrl(postId));
        article.setTag_list(this.getTagList(postId));

        getMealInfo(mealId);
        article.setMeal_info(this.getMealInfo(mealId));
        article.setRoutine_info(routineService.getRoutineInfo(routineId));
        article.setComments(this.getCommentList(postId));
        return article;
    }

    private String[] getPostImageUrl(Long postId){
        List<PostImage> images = postImageRepository.findByPostId(postId);
        String[] result = new String[images.size()];

        for(int i = 0; i < images.size(); i++){
            result[i] = images.get(i).getImageUrl();
        }

        return result;
    }

    private String[] getTagList(Long postId){
        List<Tag> tags = tagRepository.findByPostId(postId);
        String[] result = new String[tags.size()];

        for(int i = 0 ; i < tags.size(); i++){
            result[i] = tags.get(i).getTag();
        }

        return result;
    }

    private List<MealInfo[]> getMealInfo(Long mealId){
        MealInfoByArticleId mealArticle = mealService.getMealArticle(mealId);
        List<MealInfo[]> result = new ArrayList<>();

        for(int i = 0 ; i < mealArticle.getMeals().length; i++){
            MealInfo[] meal_list = mealArticle.getMeals()[i].getMeal_list();
            result.add(meal_list);
        }

        return result;
    }

    private List<Comment> getCommentList(Long postId){
        return commentRepository.findByPostId(postId);
    }

    public List<ArticleInfo> getAllPost(int limit, int reqNumber){
        List<ArticleInfo> result = new ArrayList<>();
        Pageable pagealbe = PageRequest.of(reqNumber, limit);
        Page<Post> posts = postRepository.findAll(pagealbe);
        for(Post p : posts){
            result.add(this.setArticle(p, p.getId(), p.getMeal(), p.getRoutine()));
        }
        return result;
    }
}
