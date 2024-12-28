package bth.models.exception;

import lombok.Getter;

@Getter
public class PostNotFoundException extends RuntimeException {

    private final String postId;

    public PostNotFoundException(String postId) {
        super("Post not exist");
        this.postId = postId;
    }

}
