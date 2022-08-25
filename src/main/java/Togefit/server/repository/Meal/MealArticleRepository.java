package Togefit.server.repository.Meal;

import Togefit.server.domain.Meal.MealArticle;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MealArticleRepository extends JpaRepository<MealArticle, Long> {
    @Query(value = "SELECT * FROM meal_article WHERE user_id = :userId AND YEAR(created_at) = :year AND MONTH(created_at) = :month", nativeQuery = true)
    List<MealArticle> findByUserIdAndDate(String userId, int year, int month, PageRequest pageable);
}
