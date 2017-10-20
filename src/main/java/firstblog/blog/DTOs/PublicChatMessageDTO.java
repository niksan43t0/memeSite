package firstblog.blog.DTOs;

public class PublicChatMessageDTO {

    private Integer userId;
    private String content;

    public PublicChatMessageDTO() {
    }

    public PublicChatMessageDTO(Integer userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
