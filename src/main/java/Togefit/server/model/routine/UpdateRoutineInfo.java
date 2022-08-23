package Togefit.server.model.routine;

import Togefit.server.domain.Routine.ExerciseInfo;

public class UpdateRoutineInfo {
    private Long id;
    private String routine_name;
    private ExerciseInfo[] routine_list;

    public String getRoutine_name() {
        return routine_name;
    }

    public void setRoutine_name(String routine_name) {
        this.routine_name = routine_name;
    }

    public ExerciseInfo[] getRoutine_list() {
        return routine_list;
    }

    public void setRoutine_list(ExerciseInfo[] routine_list) {
        this.routine_list = routine_list;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
