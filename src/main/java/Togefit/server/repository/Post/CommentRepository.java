package Togefit.server.repository.Post;

import Togefit.server.domain.Post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
