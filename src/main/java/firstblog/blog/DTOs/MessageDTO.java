package firstblog.blog.DTOs;

public class MessageDTO {

    private String content;

    public MessageDTO() {
    }

    public MessageDTO(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
