package Togefit.server.domain.Post;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class Post {

    public Post(){

    }

    public Post(String contents, boolean is_open,
                Long meal, Long routine){
        this.contents = contents;
        this.is_open = is_open;
        this.meal = meal;
        this.routine = routine;
    }

    public Post(String userId, String nickname, String contents, boolean is_open,
                Long meal, Long routine){
        this.userId = userId;
        this.nickname = nickname;
        this.contents = contents;
        this.is_open = is_open;
        this.routine = routine;
        this.meal = meal;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    private String nickname;
    private String contents;
    @ColumnDefault("true")
    private boolean is_open;
    @ColumnDefault("0")
    private int likeCount;
    private Long routine;
    private Long meal;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;

    public Long getRoutine() {
        return routine;
    }

    public void setRoutine(Long routine) {
        this.routine = routine;
    }

    public Long getMeal() {
        return meal;
    }

    public void setMeal(Long meal) {
        this.meal = meal;
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean getIs_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }
}
