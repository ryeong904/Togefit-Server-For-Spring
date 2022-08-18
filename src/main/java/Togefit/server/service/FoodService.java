package Togefit.server.service;

import Togefit.server.domain.Food;
import Togefit.server.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
public class FoodService {
    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository){
        this.foodRepository = foodRepository;
    }

    public String addFood(Food food){
        // 음식 이름 필수
        if(food.getName().equals("") || food.getName() == null){
            throw new InvalidParameterException("음식 이름이 반드시 필요합니다.");
        }
        // 음수 예외처리
        if(food.getCarbohydrate() < 0 || food.getFat() < 0 || food.getProtein() < 0 ||
                food.getCalories() < 0 || food.getQuantity() < 0){
            throw new InvalidParameterException("음수의 데이터는 포함될 수 없습니다.");
        }
        foodRepository.save(food);
        return food.getName();
    }
}
