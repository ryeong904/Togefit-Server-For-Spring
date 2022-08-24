package Togefit.server.controller;

import Togefit.server.model.IdInfo;
import Togefit.server.model.MealInfo;
import Togefit.server.response.OperationResponse;
import Togefit.server.service.MealService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/meals")
@RestController
public class MealController {
    final private MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping("/")
    public OperationResponse register(@RequestBody MealInfo mealInfo, HttpServletRequest request){
        OperationResponse resp = new OperationResponse();

        String userId = (String) request.getAttribute("userId");
        mealService.saveMeal(mealInfo, userId);

        resp.setResult("등록되었습니다.");
        return resp;
    }

    @DeleteMapping("/")
    public OperationResponse delete(@RequestBody IdInfo idInfo, HttpServletRequest request){
        OperationResponse resp = new OperationResponse();
        mealService.deleteMeal(idInfo.getId());
        resp.setResult("삭제되었습니다.");
        return resp;
    }
}