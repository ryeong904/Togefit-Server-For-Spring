package Togefit.server.domain.Meal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MealArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mealGroupId;
    private String userId;

    public MealArticle(String userId){
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getMealGroupId() {
        return mealGroupId;
    }

    public void setMealGroupId(Long mealGroupId) {
        this.mealGroupId = mealGroupId;
    }
}
