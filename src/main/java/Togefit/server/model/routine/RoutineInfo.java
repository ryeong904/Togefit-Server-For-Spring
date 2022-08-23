package Togefit.server.model.routine;

public class RoutineInfo {
    private String userId;
    private RoutineListInfo[] routines;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RoutineListInfo[] getRoutines() {
        return routines;
    }

    public void setRoutines(RoutineListInfo[] routines) {
        this.routines = routines;
    }
}
