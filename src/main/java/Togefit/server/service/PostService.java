package Togefit.server.service;

import Togefit.server.domain.Post.Post;
import Togefit.server.domain.Post.PostImage;
import Togefit.server.domain.Post.Tag;
import Togefit.server.model.PostInfo;
import Togefit.server.repository.Post.PostImageRepository;
import Togefit.server.repository.Post.PostRepository;
import Togefit.server.repository.Post.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostImageRepository postImageRepository;
    private final AwsS3Service awsS3Service;

    public PostService(PostRepository postRepository, TagRepository tagRepository, PostImageRepository postImageRepository, AwsS3Service awsS3Service) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postImageRepository = postImageRepository;
        this.awsS3Service = awsS3Service;
    }

    public void addPost(PostInfo post, String tag_list, List<MultipartFile> multipartFiles) throws IOException {
        // post 저장 -> postId로 (not null) tag_list 저장 (콤마 기준으로 분리하기), (not null) multipartFile 저장..

        Post newPost = new Post(post.getUserId(), post.getNickname(), post.getContents(),
                post.isIs_open(), post.getMeal(),post.getRoutine());

        postRepository.save(newPost);

        Long postId = newPost.getId();

        if(tag_list != null){
            saveTag(tag_list,postId);
        }

        if(!multipartFiles.isEmpty()){
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

    private void saveImageFiles(Long postId, List<MultipartFile> multipartFiles) throws IOException {
        List<String> imageFiles = this.awsS3Service.multipleUploadFile(multipartFiles);
        for(String i : imageFiles){
            PostImage postImage = new PostImage(postId, i);
            postImageRepository.save(postImage);
        }
    }

    private String[] getTagList(String tags){
        return tags.split(",");
    }
}
