package Togefit.server.model.Post;

import Togefit.server.domain.Post.Comment;
import Togefit.server.model.meal.MealInfo;
import Togefit.server.model.routine.RoutineListInfo;

import java.util.List;

public class ArticleInfo {
    public ArticleInfo(){}
    public ArticleInfo(String userId, String nickname, String contents, boolean is_open,
                       int likieCount){
        this.userId = userId;
        this.nickname = nickname;
        this.contents = contents;
        this.likeCount = likieCount;
        this.is_open = is_open;
    }
    private String userId;
    private String nickname;
    private String contents;
    private String[] post_image;
    private boolean is_open;
    private String[] tag_list;
    private int likeCount;
    private List<MealInfo[]> meal_info;
    private RoutineListInfo routine_info;
    private List<Comment> comments;


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

    public String[] getPost_image() {
        return post_image;
    }

    public void setPost_image(String[] post_image) {
        this.post_image = post_image;
    }

    public boolean isIs_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

    public String[] getTag_list() {
        return tag_list;
    }

    public void setTag_list(String[] tag_list) {
        this.tag_list = tag_list;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<MealInfo[]> getMeal_info() {
        return meal_info;
    }

    public void setMeal_info(List<MealInfo[]> meal_info) {
        this.meal_info = meal_info;
    }

    public RoutineListInfo getRoutine_info() {
        return routine_info;
    }

    public void setRoutine_info(RoutineListInfo routine_info) {
        this.routine_info = routine_info;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
