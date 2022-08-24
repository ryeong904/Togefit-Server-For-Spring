package Togefit.server.domain.Meal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MealArray {

    public MealArray(){

    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mealGroupId;

    public Long getMealGroupId() {
        return mealGroupId;
    }

    public void setMealGroupId(Long mealGroupId) {
        this.mealGroupId = mealGroupId;
    }
}
