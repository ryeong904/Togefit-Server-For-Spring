package Togefit.server.service;

import Togefit.server.domain.Meal.Meal;
import Togefit.server.domain.Meal.MealArray;
import Togefit.server.domain.Meal.MealArticle;
import Togefit.server.model.meal.MealInfo;
import Togefit.server.model.meal.MealInfoByArticleId;
import Togefit.server.model.meal.MealList;
import Togefit.server.model.meal.Meals;
import Togefit.server.repository.Meal.MealArrayRepository;
import Togefit.server.repository.Meal.MealArticleRepository;
import Togefit.server.repository.Meal.MealRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public void saveMeal(Meals mealInfo, String userId){
        MealArticle article = new MealArticle(userId, new Date());
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
    public void deleteMeal(Long articleId){
        // mealArticle -> meal -> mealArray 순서로 삭제

        mealArticleRepository.deleteById(articleId);

        List<Meal> findMeal = mealRepository.findByArticleId(articleId);
        HashSet<Long> set = getGroupId(findMeal);
        mealRepository.deleteByArticleId(articleId);

        for(Long l : set){
            mealArrayRepository.deleteById(l);
        }
    }

    private HashSet<Long> getGroupId(List<Meal> meal){
        HashSet<Long> set = new HashSet<>();
        for(Meal m : meal){
            set.add(m.getMealGroupId());
        }
        return set;
    }

    public MealInfoByArticleId getMealArticle(Long articleId){
        MealInfoByArticleId article = new MealInfoByArticleId();
        Optional<MealArticle> findMealArticle = this.findOne(articleId);

        // 아이디 설정
        article.setUserId(findMealArticle.get().getUserId());

        List<Meal> findMeal = mealRepository.findByArticleId(articleId);
        HashSet<Long> set = getGroupId(findMeal);

        MealList[] mealList = new MealList[set.size()];

        for(int i=0; i<mealList.length; i++){
            for(Long l : set){
                List<Meal> list = mealRepository.findByMealGroupId(l);
                MealList newMealList = new MealList();
                newMealList.setMeal_list(new MealInfo[list.size()]);

                int index = 0;
                for(Meal m : list){
                    MealInfo meal = new MealInfo(m.getFoodName(), m.getQuantity());
                    newMealList.getMeal_list()[index] = meal;
                    index += 1;
                }
                mealList[i] = newMealList;
            }
        }
        article.setMeals(mealList);
        return article;
    }

    public Object[] getAllMeals(){
        List<MealArticle> articles = mealArticleRepository.findAll();
        Object[] obj = new Object[articles.size()];
        for(int i = 0 ; i < articles.size(); i++){
            obj[i] = this.getMealArticle(articles.get(i).getId());
        }
        return obj;
    }

    private Optional<MealArticle> findOne(Long articleId){
        return mealArticleRepository.findById(articleId);
    }
}
