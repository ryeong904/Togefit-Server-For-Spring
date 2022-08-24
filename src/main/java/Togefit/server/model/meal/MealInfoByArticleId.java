package Togefit.server.model.meal;

public class MealInfoByArticleId {
    private String userId;
    private MealList[] meals;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MealList[] getMeals() {
        return meals;
    }

    public void setMeals(MealList[] meals) {
        this.meals = meals;
    }

}
