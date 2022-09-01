package Togefit.server.domain.Post;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Liked {

    public Liked(){}

    public Liked(Long postId, String userId){
        this.postId = postId;
        this.userId = userId;
    }

    @Id
    private Long postId;
    private String userId;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
