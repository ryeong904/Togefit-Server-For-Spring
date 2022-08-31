package Togefit.server.repository.Post;

import Togefit.server.domain.Post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    void deleteByPostId(Long postId);
    List<PostImage> findByPostId(Long postId);
}
