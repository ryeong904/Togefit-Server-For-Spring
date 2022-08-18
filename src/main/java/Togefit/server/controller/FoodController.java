package Togefit.server.controller;

import Togefit.server.domain.Food;
import Togefit.server.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/foods")
@RestController
public class FoodController {

    final private FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService){
        this.foodService = foodService;
    }

    @PostMapping("/")
    public String register(@RequestBody Food newFood){
        return foodService.addFood(newFood);
    }

}
