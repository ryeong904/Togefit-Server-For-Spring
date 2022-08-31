package Togefit.server.repository.Post;

import Togefit.server.domain.Post.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    void deleteByPostId(Long postId);
    List<Tag> findByPostId(Long postId);
}
