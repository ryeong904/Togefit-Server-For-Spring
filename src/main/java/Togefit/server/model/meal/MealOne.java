package Togefit.server.model.meal;

public class MealOne {
    private Long mealArticleId;
    private MealInfo[] meals;

    public MealInfo[] getMeals() {
        return meals;
    }

    public void setMeals(MealInfo[] meals) {
        this.meals = meals;
    }

    public Long getMealArticleId() {
        return mealArticleId;
    }

    public void setMealArticleId(Long mealArticleId) {
        this.mealArticleId = mealArticleId;
    }
}
