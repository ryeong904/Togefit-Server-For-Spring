package Togefit.server.repository.Post;

import Togefit.server.domain.Post.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long> {
    Optional<Liked> findByPostIdAndUserId(Long postId, String userId);
}
