package Togefit.server.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    private String name;
    private String nickname;
    @Id
    private String userId;
    private String password;
//    private String profile_image;
//    private String[] liked;
//    private String refresh_token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public String getProfile_image() {
//        return profile_image;
//    }
//
//    public void setProfile_image(String profile_image) {
//        this.profile_image = profile_image;
//    }
//
//    public String[] getLiked() {
//        return liked;
//    }
//
//    public void setLiked(String[] liked) {
//        this.liked = liked;
//    }
//
//    public String getRefresh_token() {
//        return refresh_token;
//    }
//
//    public void setRefresh_token(String refresh_token) {
//        this.refresh_token = refresh_token;
//    }
}
