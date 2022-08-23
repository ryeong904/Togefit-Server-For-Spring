package Togefit.server.service;

import Togefit.server.domain.Meal.Meal;
import Togefit.server.domain.Meal.MealArticle;
import Togefit.server.model.MealInfo;
import Togefit.server.repository.Meal.MealArticleRepository;
import Togefit.server.repository.Meal.MealRepository;
import org.springframework.stereotype.Service;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final MealArticleRepository mealArticleRepository;

    public MealService(MealRepository mealRepository, MealArticleRepository mealArticleRepository) {
        this.mealRepository = mealRepository;
        this.mealArticleRepository = mealArticleRepository;
    }

    public void saveMeal(MealInfo mealInfo, String userId){
        for(int i = 0; i < mealInfo.getMeals().length; i++){
            MealArticle mealArticle = new MealArticle(userId);
            mealArticleRepository.save(mealArticle);
            Long groupId = mealArticle.getMealGroupId();

            Meal[][] meals = mealInfo.getMeals();
            for(int j = 0 ; j < meals[i].length; j++){
                Meal meal = new Meal(groupId, meals[i][j].getFoodName(), meals[i][j].getQuantity());
                mealRepository.save(meal);
            }
        }
    }
}
