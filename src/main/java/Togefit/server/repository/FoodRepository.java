package Togefit.server.repository;

import Togefit.server.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByNameContaining(String foodName);
}
