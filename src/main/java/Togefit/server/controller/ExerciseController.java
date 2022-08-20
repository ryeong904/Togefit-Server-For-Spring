package Togefit.server.controller;

import Togefit.server.domain.Exercise;
import Togefit.server.response.OperationResponse;
import Togefit.server.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/exercises")
@RestController
public class ExerciseController {

    final private ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping("/")
    public OperationResponse register(@RequestBody Exercise exercise){
        OperationResponse resp = new OperationResponse();
        resp.setResult(exerciseService.addExercise(exercise) + "가(이) 등록되었습니다.");
        return resp;
    }

    @GetMapping("/")
    public List<Exercise> getExerciseList(){
        return exerciseService.getExerciseList();
    }

    @DeleteMapping("/")
    public OperationResponse delete(@RequestBody Exercise exercise){
        OperationResponse resp = new OperationResponse();
        exerciseService.deleteExercise(exercise.getName());
        resp.setResult("운동이 정상적으로 삭제되었습니다.");
        return resp;
    }
}
