package firstblog.blog.service;

import firstblog.blog.DTOs.MessageDTO;
import firstblog.blog.DTOs.PublicChatMessageDTO;
import org.springframework.ui.Model;

public interface ChatService {

    String loadPublicChat(Model model);
    MessageDTO processMessage(PublicChatMessageDTO message);
}
