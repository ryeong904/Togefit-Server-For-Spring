package Togefit.server.domain.Meal;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class MealArticle {

    public MealArticle(){

    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;

    public MealArticle(String userId, Calendar createdAt){
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }
}
