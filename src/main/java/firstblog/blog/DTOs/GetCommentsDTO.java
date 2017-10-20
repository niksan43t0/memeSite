package firstblog.blog.DTOs;

import firstblog.blog.entity.Comment;
import org.springframework.data.domain.Page;

public class GetCommentsDTO {

    private final Page<Comment> commentPage;
    private final boolean loggedUserAdminOrMemeAuthor;
    private final Integer loggedUserId;

    public GetCommentsDTO(Page<Comment> commentPage, boolean loggedUserAdminOrMemeAuthor, Integer loggedUserId) {
        this.commentPage = commentPage;
        this.loggedUserAdminOrMemeAuthor = loggedUserAdminOrMemeAuthor;
        this.loggedUserId = loggedUserId;
    }

    public Page<Comment> getCommentPage() {
        return commentPage;
    }

    public boolean isLoggedUserAdminOrMemeAuthor() {
        return loggedUserAdminOrMemeAuthor;
    }

    public Integer getLoggedUserId() {
        return loggedUserId;
    }
}
