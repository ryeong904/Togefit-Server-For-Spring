package Togefit.server.model.meal;

public class MealOne {
    private Long mealArticleId;
    private Long mealListId;
    private MealInfo[] meals;

    public Long getMealArticleId() {
        return mealArticleId;
    }

    public void setMealArticleId(Long mealArticleId) {
        this.mealArticleId = mealArticleId;
    }


    public MealInfo[] getMeals() {
        return meals;
    }

    public void setMeals(MealInfo[] meals) {
        this.meals = meals;
    }

    public Long getMealListId() {
        return mealListId;
    }

    public void setMealListId(Long mealListId) {
        this.mealListId = mealListId;
    }
}
