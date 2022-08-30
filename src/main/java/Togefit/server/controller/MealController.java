package Togefit.server.controller;

import Togefit.server.model.IdInfo;
import Togefit.server.model.meal.MealInfoByArticleId;
import Togefit.server.model.meal.MealOne;
import Togefit.server.model.meal.Meals;
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
    public OperationResponse register(@RequestBody Meals mealInfo, HttpServletRequest request){
        OperationResponse resp = new OperationResponse();

        String userId = (String) request.getAttribute("userId");
        mealService.saveMeal(mealInfo, userId);

        resp.setResult("등록되었습니다.");
        return resp;
    }

    @DeleteMapping("/")
    public OperationResponse delete(@RequestBody IdInfo idInfo, HttpServletRequest request){
        OperationResponse resp = new OperationResponse();
        String userId = (String) request.getAttribute("userId");
        mealService.deleteMeal(idInfo.getId(), userId);
        resp.setResult("삭제되었습니다.");
        return resp;
    }


    @GetMapping("/{articleId}")
    public MealInfoByArticleId get(@PathVariable Long articleId){
        MealInfoByArticleId article = new MealInfoByArticleId();
        return mealService.getMealArticle(articleId);
    }

    @GetMapping("/all")
    public Object[] getAll(){
        return mealService.getAllMeals();
    }

    @GetMapping("/")
    public Object[] getPagenation(
            @RequestParam("userId") String userId,
            @RequestParam("limit") int limit,
            @RequestParam("reqNumber") int reqNumber,
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ){
        return mealService.getPagenation(userId, limit, reqNumber, year, month);
    }

    @PostMapping("/one")
    public OperationResponse registerOne(@RequestBody MealOne meal, HttpServletRequest request){
        OperationResponse resp = new OperationResponse();
        String userId = (String) request.getAttribute("userId");
        mealService.saveMealOne(meal, userId);

        resp.setResult("등록되었습니다.");
        return resp;
    }

    @PatchMapping("/one")
    public OperationResponse updateOne(@RequestBody MealOne meal, HttpServletRequest request){
        OperationResponse resp = new OperationResponse();
        String userId = (String) request.getAttribute("userId");

        mealService.updateMealOne(meal, userId);

        resp.setResult("수정되었습니다.");
        return resp;
    }

    @DeleteMapping("/one")
    public OperationResponse deleteOne(@RequestBody MealOne meal, HttpServletRequest request){
        OperationResponse resp = new OperationResponse();
        String userId = (String) request.getAttribute("userId");

        mealService.deleteMealOne(meal.getMealListId(), userId);
        resp.setResult("삭제되었습니다.");
        return resp;
    }
}
