package Togefit.server.model;

import Togefit.server.domain.Meal.Meal;

public class MealInfo {
    private Meal[][] meals;

    public Meal[][] getMeals() {
        return meals;
    }

    public void setMeals(Meal[][] meals) {
        this.meals = meals;
    }
}
