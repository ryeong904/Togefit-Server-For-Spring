package Togefit.server.service;

import Togefit.server.domain.Meal.Meal;
import Togefit.server.domain.Meal.MealArray;
import Togefit.server.domain.Meal.MealArticle;
import Togefit.server.model.MealInfo;
import Togefit.server.repository.Meal.MealArrayRepository;
import Togefit.server.repository.Meal.MealArticleRepository;
import Togefit.server.repository.Meal.MealRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final MealArrayRepository mealArrayRepository;
    private final MealArticleRepository mealArticleRepository;

    public MealService(MealRepository mealRepository, MealArrayRepository mealArrayRepository, MealArticleRepository mealArticleRepository) {
        this.mealRepository = mealRepository;
        this.mealArrayRepository = mealArrayRepository;
        this.mealArticleRepository = mealArticleRepository;
    }

    public void saveMeal(MealInfo mealInfo, String userId){
        MealArticle article = new MealArticle(userId);
        mealArticleRepository.save(article);
        Long articleId = article.getId();
        for(int i = 0; i < mealInfo.getMeals().length; i++){
            MealArray mealArray = new MealArray();
            mealArrayRepository.save(mealArray);
            Long mealGroupId = mealArray.getMealGroupId();

            Meal[][] meals = mealInfo.getMeals();
            for(int j = 0 ; j < meals[i].length; j++){
                Meal meal = new Meal(mealGroupId, meals[i][j].getFoodName(), meals[i][j].getQuantity(), articleId);
                mealRepository.save(meal);
            }
        }
    }

    @Transactional
    public void deleteMeal(Long mealId){
        mealArticleRepository.deleteById(mealId);
//        mealRepository.deleteByMealGroupId(mealId);
    }
}
