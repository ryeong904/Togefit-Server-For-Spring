package Togefit.server.service;

import Togefit.server.domain.Routine.ExerciseInfo;
import Togefit.server.domain.Routine.Routine;
import Togefit.server.domain.Routine.RoutineList;
import Togefit.server.model.RoutineListInfo;
import Togefit.server.repository.Routine.ExerciseInfoRepository;
import Togefit.server.repository.Routine.RoutineListRepository;
import Togefit.server.repository.Routine.RoutineRepository;
import org.springframework.stereotype.Service;

@Service
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineListRepository routineListRepository;
    private final ExerciseInfoRepository exerciseInfoRepository;

    public RoutineService(RoutineRepository routineRepository, RoutineListRepository routineListRepository, ExerciseInfoRepository exerciseInfoRepository) {
        this.routineRepository = routineRepository;
        this.routineListRepository = routineListRepository;
        this.exerciseInfoRepository = exerciseInfoRepository;
    }

    public String saveRoutineList(RoutineListInfo routineListInfo, String userId){
//        Optional<Routine> findRoutine = routineRepository.findByUserId(routine.getUserId());
//
//        Routine r = new Routine(routine.getUserId());
//        // 비어있으면 빈 배열 생성해서 추가
//        if(findRoutine.isEmpty()){
//            routineRepository.save(new Routine(routine.getUserId()));
//        }
//
//        routineRepository.save(routine);
        RoutineList newRoutineList = new RoutineList(routineListInfo.getRoutineName());
        routineListRepository.save(newRoutineList);

        Long routineListId = newRoutineList.getRoutineListId();
        this.saveRoutineInfo(routineListId, routineListInfo.getRoutineList());

        Routine newRoutine = new Routine(userId);
        newRoutine.setRoutineListId(routineListId);
        routineRepository.save(newRoutine);

        return routineListInfo.getRoutineName();
    }

    public void saveRoutineInfo(Long id, ExerciseInfo[] infos){
        for(int i = 0 ; i < infos.length; i++){
            ExerciseInfo newExerciseInfo = new ExerciseInfo();
            newExerciseInfo.setRoutineListId(id);
            newExerciseInfo.setName(infos[i].getName());
            newExerciseInfo.setCount(infos[i].getCount());
            newExerciseInfo.setWeight(infos[i].getWeight());
            newExerciseInfo.setSetCount(infos[i].getSetCount());
            exerciseInfoRepository.save(newExerciseInfo);
        }
    }

}
