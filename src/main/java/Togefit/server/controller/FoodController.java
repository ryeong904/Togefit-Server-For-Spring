package Togefit.server.controller;

import Togefit.server.domain.Food;
import Togefit.server.model.IdInfo;
import Togefit.server.response.OperationResponse;
import Togefit.server.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/foods")
@RestController
public class FoodController {

    final private FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService){
        this.foodService = foodService;
    }

    @PostMapping("/")
    public OperationResponse register(@RequestBody Food newFood){
        OperationResponse resp = new OperationResponse();
        resp.setResult(foodService.addFood(newFood) + "가(이) 등록되었습니다.");
        return resp;
    }

    @DeleteMapping("/")
    public OperationResponse delete(@RequestBody IdInfo idInfo){
        OperationResponse resp = new OperationResponse();
        foodService.deleteFood(idInfo.getId());
        resp.setResult("음식이 정상적으로 삭제되었습니다.");
        return resp;
    }

    @GetMapping("/")
    public List<Food> getFoodList(){
        return foodService.getFoodList();
    }

    @PatchMapping("/")
    public OperationResponse update(@RequestBody Food food){
        OperationResponse resp = new OperationResponse();
        foodService.updateFood(food);
        resp.setResult("음식 정보가 정상적으로 수정되었습니다.");
        return resp;
    }

}
