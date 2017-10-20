package firstblog.blog.service;

import firstblog.blog.DTOs.MessageDTO;
import firstblog.blog.DTOs.PublicChatMessageDTO;
import firstblog.blog.entity.User;
import firstblog.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ChatServiceImp implements ChatService {

    private UserRepository userRepository;

    @Autowired
    public ChatServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String loadPublicChat(Model model){
        if(!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            User userEntity = this.userRepository.findByEmail(user.getUsername());
            model.addAttribute("userId", userEntity.getId());
        }
        return "chat/public-chat";
    }

    @Override
    public MessageDTO processMessage(PublicChatMessageDTO message){
        String processedMessage = this.userRepository.findById(message.getUserId()).getFullName() + ": " + message.getContent();
        return new MessageDTO(processedMessage);
    }
}
