package Togefit.server.domain.Meal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Meal {
    public Meal(Long mealArrayId, String foodName, Integer quantity){
        this.mealArrayId = mealArrayId;
        this.foodName = foodName;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long mealArrayId;
    private String foodName;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMealArrayId() {
        return mealArrayId;
    }

    public void setMealArrayId(Long mealArrayId) {
        this.mealArrayId = mealArrayId;
    }
}
