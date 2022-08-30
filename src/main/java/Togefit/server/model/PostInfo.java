package Togefit.server.model;

public class PostInfo {

    public PostInfo(String userId, String nickname, String contents, boolean is_open, Long meal, Long routine){
        this.userId = userId;
        this.nickname = nickname;
        this.contents = contents;
        this.is_open = is_open;
        this.meal = meal;
        this.routine = routine;
    }
    private String userId;
    private String nickname;
    private String contents;
    private boolean is_open;
    private Long routine;
    private Long meal;


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

    public boolean isIs_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

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

}
