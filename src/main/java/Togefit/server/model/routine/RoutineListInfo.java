package Togefit.server.model.routine;

import Togefit.server.domain.Routine.ExerciseInfo;

public class RoutineListInfo {
    private String routineName;
    private ExerciseInfo[] routineList;

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public ExerciseInfo[] getRoutineList() {
        return routineList;
    }

    public void setRoutineList(ExerciseInfo[] routineList) {
        this.routineList = routineList;
    }
}
