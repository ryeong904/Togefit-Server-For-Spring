package Togefit.server.domain.Routine;

import javax.persistence.*;
@Entity
public class Routine {
    public Routine(){

    }
    public Routine(String userId){
        this.userId = userId;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    private Long routineListId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getRoutineListId() {
        return routineListId;
    }

    public void setRoutineListId(Long routineListId) {
        this.routineListId = routineListId;
    }
}
