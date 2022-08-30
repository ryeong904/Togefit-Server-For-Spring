package Togefit.server.repository.Post;

import Togefit.server.domain.Post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    void deleteByPostId(Long postId);
}
