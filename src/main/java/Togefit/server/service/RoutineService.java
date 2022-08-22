package Togefit.server.service;

import Togefit.server.domain.Routine.ExerciseInfo;
import Togefit.server.domain.Routine.Routine;
import Togefit.server.domain.Routine.RoutineList;
import Togefit.server.model.RoutineInfo;
import Togefit.server.model.RoutineListInfo;
import Togefit.server.repository.Routine.ExerciseInfoRepository;
import Togefit.server.repository.Routine.RoutineListRepository;
import Togefit.server.repository.Routine.RoutineRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        RoutineList newRoutineList = new RoutineList(routineListInfo.getRoutineName());
        routineListRepository.save(newRoutineList);

        Long routineListId = newRoutineList.getRoutineListId();
        this.saveRoutineInfo(routineListId, routineListInfo.getRoutineList());

        Routine newRoutine = new Routine(userId);
        newRoutine.setRoutineListId(routineListId);
        routineRepository.save(newRoutine);

        return routineListInfo.getRoutineName();
    }

    private void saveRoutineInfo(Long id, ExerciseInfo[] infos){
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

    public RoutineInfo getRoutine(String userId){
        RoutineInfo routines = new RoutineInfo();
        routines.setUserId(userId);

        List<Routine> routine = routineRepository.findByUserId(userId);

        RoutineListInfo[] newRoutineListInfo = new RoutineListInfo[routine.size()];

        for(int i = 0 ; i < routine.size(); i++){
            Long routineListId = routine.get(i).getRoutineListId();

            // 루틴 이름 설정
            Optional<RoutineList> routinelist = routineListRepository.findById(routineListId);
            newRoutineListInfo[i] = new RoutineListInfo();
            newRoutineListInfo[i].setRoutineName(routinelist.get().getRoutineName());

            List<ExerciseInfo> exerciseInfos = exerciseInfoRepository.findByRoutineListId(routineListId);
            List<ExerciseInfo> newExercistInfo = new ArrayList<>();

            for(int j = 0; j < exerciseInfos.size(); j++){
                ExerciseInfo exInfo = new ExerciseInfo();
                exInfo.setName(exerciseInfos.get(j).getName());
                exInfo.setWeight(exerciseInfos.get(j).getWeight());
                exInfo.setCount(exerciseInfos.get(j).getCount());
                exInfo.setSetCount(exerciseInfos.get(j).getSetCount());
                newExercistInfo.add(exInfo);
            }

            ExerciseInfo[] exercistInfo = exerciseInfos.toArray(new ExerciseInfo[newExercistInfo.size()]);
            newRoutineListInfo[i].setRoutineList(exercistInfo);
        }

        routines.setRoutines(newRoutineListInfo);
        return routines;
    }

}
