package Togefit.server.service;

import Togefit.server.domain.Food;
import Togefit.server.repository.FoodRepository;
import Togefit.server.response.error.CustomException;
import Togefit.server.response.error.Error;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {
    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository){
        this.foodRepository = foodRepository;
    }

    public String addFood(Food food){
        // 음식 이름 필수
        if(food.getName().equals("")){
            throw new CustomException(new Error("음식 이름이 반드시 필요합니다."));
        }

        food = foodNullCheck(food);

        // 음수 예외처리
        if(food.getCarbohydrate() < 0 || food.getFat() < 0 || food.getProtein() < 0 ||
                food.getCalories() < 0 || food.getQuantity() < 0){
            throw new CustomException(new Error("음수의 데이터는 포함될 수 없습니다."));
        }
        foodRepository.save(food);
        return food.getName();
    }

    public void deleteFood(long id){
        Optional<Food> findFood = this.findOne(id);

        if(findFood.isEmpty()){
            throw new CustomException(new Error("해당 음식을 찾지 못했습니다."));
        }
        foodRepository.delete(findFood.get());
    }

    public Optional<Food> findOne(Long id){
        return foodRepository.findById(id);
    }

    public List<Food> getFoodList(){
        return foodRepository.findAll();
    }

    public void updateFood(Food food){
        Optional<Food> findFood = this.findOne(food.getId());

        if(findFood.isEmpty()){
            throw new CustomException(new Error("해당 음식을 찾지 못했습니다."));
        }

        Food updateFood = updateFoodInfo(findFood.get(), food);

        foodRepository.save(updateFood);

    }

    private Food foodNullCheck(Food food){
        if(food.getCarbohydrate() == null) food.setCarbohydrate(0);
        if(food.getFat() == null) food.setFat(0);
        if(food.getProtein() == null) food.setProtein(0);
        if(food.getCalories() == null) food.setCalories(0);
        if(food.getQuantity() == null) food.setQuantity(0);
        return food;
    }

    private Food updateFoodInfo(Food food, Food updateFood){
        if(updateFood.getCarbohydrate() != null) food.setCarbohydrate(updateFood.getCarbohydrate());
        if(updateFood.getFat() != null) food.setFat(updateFood.getFat());
        if(updateFood.getProtein() != null) food.setProtein(updateFood.getProtein());
        if(updateFood.getCalories() != null) food.setCalories(updateFood.getCalories());
        if(updateFood.getQuantity() != null) food.setQuantity(updateFood.getQuantity());

        return food;
    }

    public List<Food> searchFood(String foodName){
        return foodRepository.findByNameContaining(foodName);
    }
}
