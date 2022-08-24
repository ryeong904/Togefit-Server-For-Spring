package Togefit.server.domain.Meal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Meal {

    public Meal(Long mealGroupId, String foodName, Integer quantity, Long articleId){
        this.mealGroupId = mealGroupId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.articleId = articleId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long mealGroupId;
    private String foodName;
    private Integer quantity;
    private Long articleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMealGroupId() {
        return mealGroupId;
    }

    public void setMealGroupId(Long mealGroupId) {
        this.mealGroupId = mealGroupId;
    }

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

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
}
