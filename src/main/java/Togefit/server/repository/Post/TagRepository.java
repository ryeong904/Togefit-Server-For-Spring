package Togefit.server.repository.Post;

import Togefit.server.domain.Post.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
