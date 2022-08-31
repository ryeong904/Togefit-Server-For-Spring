package Togefit.server.service;

import Togefit.server.domain.Routine.ExerciseInfo;
import Togefit.server.domain.Routine.Routine;
import Togefit.server.domain.Routine.RoutineList;
import Togefit.server.model.routine.RoutineInfo;
import Togefit.server.model.routine.RoutineListInfo;
import Togefit.server.model.routine.UpdateRoutineInfo;
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
    private final UserService userService;
    private final RoutineRepository routineRepository;
    private final RoutineListRepository routineListRepository;
    private final ExerciseInfoRepository exerciseInfoRepository;

    public RoutineService(UserService userService, RoutineRepository routineRepository, RoutineListRepository routineListRepository, ExerciseInfoRepository exerciseInfoRepository) {
        this.userService = userService;
        this.routineRepository = routineRepository;
        this.routineListRepository = routineListRepository;
        this.exerciseInfoRepository = exerciseInfoRepository;
    }

    public String saveRoutineList(RoutineListInfo routineListInfo, String userId){
        if(userService.findOne(userId).isEmpty()){
            throw new CustomException(new Error("해당 유저를 찾지 못했습니다."));
        }

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
        List<ExerciseInfo> newExerciseInfo = new ArrayList<>();

        // 유효한 값인지 검사
        for(int i = 0 ; i < infos.length; i++){
            if(infos[i].getCount() < 0 || infos[i].getWeight() < 0 || infos[i].getSetCount() <0){
                throw new CustomException(new Error("0보다 큰 숫자를 입력해주세요."));
            }
            ExerciseInfo info = new ExerciseInfo(id, infos[i].getName(), infos[i].getCount(), infos[i].getSetCount(), infos[i].getWeight());
            newExerciseInfo.add(info);
        }

        for(ExerciseInfo info : newExerciseInfo){
            exerciseInfoRepository.save(info);
        }
    }

    public RoutineInfo getRoutine(String userId){
        if(userService.findOne(userId).isEmpty()){
            throw new CustomException(new Error("해당 유저를 찾지 못했습니다."));
        }
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

    @Transactional
    public void updateRoutine(UpdateRoutineInfo updateRoutineInfo, String userId){
        Optional<Routine> findRoutine = routineRepository.findByRoutineListId(updateRoutineInfo.getId());
        if(findRoutine.isEmpty()){
            throw new CustomException(new Error("해당 루틴을 찾지 못했습니다."));
        }

        if(!findRoutine.get().getUserId().equals(userId)){
            throw new CustomException(new Error(403, "작성자만 수정할 수 있습니다."));
        }

        // 이름이 null이 아닐 경우 update
        if(updateRoutineInfo.getRoutine_name() != null){
            routineListRepository.save(new RoutineList(updateRoutineInfo.getId(), updateRoutineInfo.getRoutine_name()));
        }

        // 루틴 내용 update
        if(updateRoutineInfo.getRoutine_list() != null){
            exerciseInfoRepository.deleteByRoutineListId(updateRoutineInfo.getId());
            saveRoutineInfo(updateRoutineInfo.getId(), updateRoutineInfo.getRoutine_list());
        }
    }

    public RoutineListInfo getRoutineInfo(Long routineId){
        RoutineListInfo routineListInfo = new RoutineListInfo();
        Optional<RoutineList> routineList = routineListRepository.findById(routineId);
        routineListInfo.setRoutineName(routineList.get().getRoutineName());

        List<ExerciseInfo> exerciseInfoList = exerciseInfoRepository.findByRoutineListId(routineId);

        ExerciseInfo[] temp = exerciseInfoList.toArray(new ExerciseInfo[exerciseInfoList.size()]);
        routineListInfo.setRoutineList(temp);
        return routineListInfo;
    }
}
