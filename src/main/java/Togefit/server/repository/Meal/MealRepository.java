package Togefit.server.repository.Meal;

import Togefit.server.domain.Meal.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
