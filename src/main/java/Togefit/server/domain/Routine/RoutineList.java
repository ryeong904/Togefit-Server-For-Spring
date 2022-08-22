package Togefit.server.domain.Routine;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RoutineList {
    public RoutineList(){

    }
    public RoutineList(String routineName){
        this.routineName = routineName;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long routineListId;
    private String routineName;

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public Long getRoutineListId() {
        return routineListId;
    }
}
