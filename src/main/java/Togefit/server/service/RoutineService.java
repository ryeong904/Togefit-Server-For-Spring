package Togefit.server.service;

import Togefit.server.domain.Routine.ExerciseInfo;
import Togefit.server.domain.Routine.Routine;
import Togefit.server.domain.Routine.RoutineList;
import Togefit.server.model.RoutineInfo;
import Togefit.server.model.RoutineListInfo;
import Togefit.server.repository.Routine.ExerciseInfoRepository;
import Togefit.server.repository.Routine.RoutineListRepository;
import Togefit.server.repository.Routine.RoutineRepository;
import Togefit.server.response.error.CustomException;
import Togefit.server.response.error.Error;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

        List<RoutineListInfo> newRoutineListInfo = new ArrayList<>();

        for(int i = 0 ; i < routine.size(); i++){
            Long routineListId = routine.get(i).getRoutineListId();

            // 루틴 이름 설정
            Optional<RoutineList> routineList = routineListRepository.findById(routineListId);

            if(routineList.isEmpty()){
                continue;
            }

            RoutineListInfo routineListInfo = new RoutineListInfo();
            routineListInfo.setRoutineName(routineList.get().getRoutineName());

            // 루틴 내용 설정
            // exerciseInfos : exerciseInfo에서 루틴 번호로 찾은 리스트들
            List<ExerciseInfo> exerciseInfos = exerciseInfoRepository.findByRoutineListId(routineListId);

            ExerciseInfo[] exerciseInfo = exerciseInfos.toArray(new ExerciseInfo[exerciseInfos.size()]);
            routineListInfo.setRoutineList(exerciseInfo);

            newRoutineListInfo.add(routineListInfo);
        }

        routines.setRoutines(newRoutineListInfo.toArray(new RoutineListInfo[newRoutineListInfo.size()]));
        return routines;
    }

    public RoutineInfo searchRoutine(String userId, String routineName){
        RoutineInfo routines = new RoutineInfo();
        routines.setUserId(userId);

        List<Routine> routine = routineRepository.findByUserId(userId);

        List<RoutineListInfo> newRoutineListInfo = new ArrayList<>();

        for(int i = 0 ; i < routine.size(); i++){
            Long routineListId = routine.get(i).getRoutineListId();

            Optional<RoutineList> routineList = routineListRepository.findById(routineListId);

            if(routineList.isEmpty() || !routineList.get().getRoutineName().contains(routineName)){
                continue;
            }

            RoutineListInfo routineListInfo = new RoutineListInfo();
            routineListInfo.setRoutineName(routineList.get().getRoutineName());

            List<ExerciseInfo> exerciseInfos = exerciseInfoRepository.findByRoutineListId(routineListId);

            ExerciseInfo[] exerciseInfo = exerciseInfos.toArray(new ExerciseInfo[exerciseInfos.size()]);
            routineListInfo.setRoutineList(exerciseInfo);

            newRoutineListInfo.add(routineListInfo);
        }

        routines.setRoutines(newRoutineListInfo.toArray(new RoutineListInfo[newRoutineListInfo.size()]));
        return routines;
    }

    private Optional<Routine> findOne(Long id){
        return routineRepository.findByRoutineListId(id);
    }

    @Transactional
    public void deleteRoutine(String userId, Long id){
        // 찾기
        Optional<Routine> findRoutine = this.findOne(id);
        if(findRoutine.isEmpty()){
            throw new CustomException(new Error( "해당 루틴을 찾지 못했습니다."));
        }

        // 유저아이디 검사(status: 403)
        if(!findRoutine.get().getUserId().equals(userId)){
            throw new CustomException(new Error(403, "작성자만 삭제할 수 있습니다."));
        }

        routineRepository.delete(findRoutine.get());
        routineListRepository.deleteById(id);
        exerciseInfoRepository.deleteByRoutineListId(id);
    }
}
