package Togefit.server.repository.Post;

import Togefit.server.domain.Post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}