package Togefit.server.controller;

import Togefit.server.domain.Exercise;
import Togefit.server.response.OperationResponse;
import Togefit.server.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
