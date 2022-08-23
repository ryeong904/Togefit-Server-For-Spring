package Togefit.server.repository.Meal;

import Togefit.server.domain.Meal.MealArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealArticleRepository extends JpaRepository<MealArticle, Long> {
}
