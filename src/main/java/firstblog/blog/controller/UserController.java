package firstblog.blog.controller;

import firstblog.blog.bindingModel.UserBindingModel;
import firstblog.blog.bindingModel.UserEditBindingModel;
import firstblog.blog.entity.Role;
import firstblog.blog.entity.User;
import firstblog.blog.repository.RoleRepository;
import firstblog.blog.repository.UserRepository;
import firstblog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model){
        return userService.loadRegisterPage(model);
    }

    @PostMapping("/register")
    public String registerProcess(UserBindingModel userBindingModel){
       return userService.registerUser(userBindingModel);
    }

    @GetMapping("/login")
    public String login(Model model){
        return userService.loadLoginPage(model);
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        return userService.loadLogoutPage(request, response);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model){
        return userService.loadProfilePage(model);
    }

    @GetMapping("/profile/edit")
    @PreAuthorize("isAuthenticated()")
    public String editProfilePage(Model model){
        return userService.editProfilePage(model);
    }

    @GetMapping("/profile/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteProfilePage(Model model){
        return userService.deleteProfilePage(model);
    }

    @PostMapping("/profile/edit")
    @PreAuthorize("isAuthenticated()")
    public String editProfile(UserBindingModel userBindingModel){
        return userService.editProfile(userBindingModel);
    }

    @PostMapping("/profile/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteProfile(@RequestParam(value = "memesDelete", required = false) String memesDelete){
        return userService.deleteProfile(memesDelete);
    }
}
