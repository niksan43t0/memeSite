package firstblog.blog.controller;

import firstblog.blog.DTOs.MessageDTO;
import firstblog.blog.DTOs.PublicChatMessageDTO;
import firstblog.blog.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class PublicChatController {

    private ChatService chatService;

    @Autowired
    public PublicChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/public-chat")
    public String index(Model model) {
        return chatService.loadPublicChat(model);
    }

    @MessageMapping("/sendMessages")
    @SendTo("/publicChat/receiveMessages")
    public MessageDTO processMessage(PublicChatMessageDTO message) throws Exception {
        return chatService.processMessage(message);
    }

}
