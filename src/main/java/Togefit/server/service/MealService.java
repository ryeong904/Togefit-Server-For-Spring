package Togefit.server.service;

import Togefit.server.domain.Meal.Meal;
import Togefit.server.domain.Meal.MealArray;
import Togefit.server.domain.Meal.MealArticle;
import Togefit.server.model.meal.*;
import Togefit.server.repository.Meal.MealArrayRepository;
import Togefit.server.repository.Meal.MealArticleRepository;
import Togefit.server.repository.Meal.MealRepository;
import Togefit.server.response.error.CustomException;
import Togefit.server.response.error.Error;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.List;

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

    private void checkMeal(Meal[][] meals){
        for(int i = 0 ; i < meals.length; i++){
            for(int j = 0 ; j < meals[i].length; j++){
                if(meals[i][j].getFoodName().length() == 0){
                    throw new CustomException(new Error("음식 이름이 반드시 필요합니다."));
                }
                if(meals[i][j].getQuantity() < 0){
                    throw new CustomException(new Error("음식의 양은 반드시 양수여야 합니다."));
                }
            }
        }
    }

    public void saveMeal(Meals mealInfo, String userId){
        checkMeal(mealInfo.getMeals());

        MealArticle article = new MealArticle(userId, Calendar.getInstance());
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
    public void deleteMeal(Long articleId, String userId){
        // mealArticle -> meal -> mealArray 순서로 삭제
        Optional<MealArticle> findArticle = mealArticleRepository.findById(articleId);

        if(findArticle.isEmpty()){
            throw new CustomException(new Error("해당 아티클을 찾지 못했습니다."));
        }

        if(!findArticle.get().getUserId().equals(userId)){
            throw new CustomException(new Error(403, "작성자만 삭제할 수 있습니다."));
        }

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

        if(findMealArticle.isEmpty()){
            throw new CustomException(new Error("해당 아티클을 찾지 못했습니다."));
        }

        // 아이디 설정
        article.setUserId(findMealArticle.get().getUserId());

        List<Meal> findMeal = mealRepository.findByArticleId(articleId);
        HashSet<Long> set = getGroupId(findMeal);

        MealList[] mealList = new MealList[set.size()];

        int i = 0;
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
            i ++;
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

    public Object[] getPagenation(String userId, int limit, int reqNumber, int year, int month){
        if(month < 1 || month > 12){
            throw new CustomException(new Error("월의 범위는 1~12입니다."));
        }

        PageRequest pageRequest = PageRequest.of(reqNumber, limit);

        List<MealArticle> articles =
                mealArticleRepository.findByUserIdAndDate(userId, year, month, pageRequest);


        Object[] obj = new Object[articles.size()];
        for(int i = 0 ; i < articles.size(); i++){
            obj[i] = this.getMealArticle(articles.get(i).getId());
        }
        return obj;
    }

    private Optional<MealArticle> findOne(Long articleId){
        return mealArticleRepository.findById(articleId);
    }

    public void saveMealOne(MealOne meal, String userId){
        // mealArray에 저장 -> 생성된 id(mealGroupId)와 articleID로 meal에 저장
        Long mealArticleId = meal.getMealArticleId();
        Optional<MealArticle> findArticle = findOne(mealArticleId);

        if(findArticle.isEmpty()){
            throw new CustomException(new Error("해당 아티클을 찾지 못했습니다."));
        }

        if(!findArticle.get().getUserId().equals(userId)){
            throw new CustomException(new Error(403, "작성자만 추가할 수 있습니다."));
        }

        MealArray mealArray = new MealArray();
        mealArrayRepository.save(mealArray);

        Long mealGroupId = mealArray.getMealGroupId();

        saveMeals(meal.getMeals(), mealGroupId, mealArticleId);
    }

    private void checkMealOne(MealInfo[] meal){
        for(int i = 0 ; i < meal.length; i++){
            if(meal[i].getFoodName().length() == 0){
                throw new CustomException(new Error("음식 이름이 반드시 필요합니다."));
            }
            if(meal[i].getQuantity() < 0){
                throw new CustomException(new Error("음식의 양은 반드시 양수여야 합니다."));
            }
        }
    }

    public void saveMeals(MealInfo[] meal, Long mealGroupId, Long mealArticleId){
        checkMealOne(meal);
        for(int i = 0 ; i < meal.length; i++){
            Meal one = new Meal(
                    mealGroupId,
                    meal[i].getFoodName(),
                    meal[i].getQuantity(),
                    mealArticleId
            );
            mealRepository.save(one);
        }
    }

    @Transactional
    public void updateMealOne(MealOne meal, String userId){
        Long mealGroupId = meal.getMealListId();

        List<Meal> meals = mealRepository.findByMealGroupId(mealGroupId);

        if(meals.size() == 0){
            throw new CustomException(new Error("해당 아티클을 찾지 못했습니다."));
        }
        Long mealArticleId = meals.get(0).getArticleId();

        mealRepository.deleteByMealGroupId(mealGroupId);

        saveMeals(meal.getMeals(), mealGroupId, mealArticleId);
    }

    @Transactional
    public void deleteMealOne(Long id, String userId){
        List<Meal> meals = mealRepository.findByMealGroupId(id);

        if(meals.size() == 0){
            throw new CustomException(new Error("해당 아티클을 찾지 못했습니다."));
        }

        Long articleId = meals.get(0).getArticleId();
        Optional<MealArticle> one = mealArticleRepository.findById(articleId);
        if(!one.get().getUserId().equals(userId)){
            throw new CustomException(new Error(403, "작성자만 삭제할 수 있습니다."));
        }
        mealRepository.deleteByMealGroupId(id);
        mealArrayRepository.deleteById(id);
    }
}
