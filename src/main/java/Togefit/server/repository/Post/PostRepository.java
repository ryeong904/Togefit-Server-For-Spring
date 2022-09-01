package Togefit.server.repository.Post;

import Togefit.server.domain.Post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long>{
    Page<Post> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM post WHERE user_id = :userId AND YEAR(created_at) = :year AND MONTH(created_at) = :month", nativeQuery = true)
    Page<Post> findByUserIdAndDate(String userId, int year, int month, Pageable pageable);
}

