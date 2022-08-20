package Togefit.server.service;

import Togefit.server.domain.Exercise;
import Togefit.server.repository.ExerciseRepository;
import Togefit.server.response.error.CustomException;
import Togefit.server.response.error.Error;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public String addExercise(Exercise exercise){
        if(exercise.getName().length() == 0){
            throw new CustomException(new Error("한 글자 이상 입력이 반드시 필요합니다."));
        }

        Optional<Exercise> findExercise = this.findOne(exercise.getName());

        if(findExercise.isPresent()){
            throw new CustomException(new Error("해당 운동은 이미 존재합니다."));
        }

        exerciseRepository.save(exercise);
        return exercise.getName();
    }

    public Optional<Exercise> findOne(String exercise){
        return exerciseRepository.findByName(exercise);
    }

    public List<Exercise> getExerciseList() {
        return exerciseRepository.findAll();
    }

    public void deleteExercise(String name){
        Optional<Exercise> findExercise = this.findOne(name);

        if(findExercise.isEmpty()){
            throw new CustomException(new Error("해당 운동을 찾지 못했습니다."));
        }

        exerciseRepository.delete(findExercise.get());
    }

    public List<Exercise> searchExercise(String exerciseName){
        return exerciseRepository.findByNameContaining(exerciseName);
    }
}
