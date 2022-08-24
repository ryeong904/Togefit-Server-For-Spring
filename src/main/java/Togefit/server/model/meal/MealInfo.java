package Togefit.server.model.meal;

public class MealInfo{
    private String foodName;
    private Integer quantity;

    public MealInfo(){

    }

    public MealInfo(String foodName, Integer quantity){
        this.foodName = foodName;
        this.quantity = quantity;
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
}