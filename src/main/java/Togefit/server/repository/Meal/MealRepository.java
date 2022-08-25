package Togefit.server.repository.Meal;

import Togefit.server.domain.Meal.Meal;
import Togefit.server.model.meal.MealInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    void deleteByArticleId(Long articleId);
    List<Meal> findByArticleId(Long articleId);
    List<Meal> findByMealGroupId(Long mealGroupId);
    void deleteByMealGroupId(Long groupId);
}
