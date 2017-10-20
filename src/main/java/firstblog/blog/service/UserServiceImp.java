package firstblog.blog.service;

import firstblog.blog.bindingModel.UserBindingModel;
import firstblog.blog.entity.Meme;
import firstblog.blog.entity.Role;
import firstblog.blog.entity.User;
import firstblog.blog.repository.RoleRepository;
import firstblog.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String loadRegisterPage(Model model) {
        model.addAttribute("view", "user/register");
        return "base-layout";
    }

    @Override
    public String registerUser(UserBindingModel userBindingModel) {
        if(this.userRepository.findByEmail(userBindingModel.getEmail())!=null)
            return "redirect:/error/email";
        if(!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword()))
            return "redirect:/error/passwordMatch";

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        try {
            User user = new User(
                    userBindingModel.getEmail(),
                    userBindingModel.getFullName(),
                    bCryptPasswordEncoder.encode(userBindingModel.getPassword()),
                    userBindingModel.getUserPicture().getBytes()
            );
            Role userRole = this.roleRepository.findByName("ROLE_USER");
            user.addRole(userRole);
            if(this.userRepository.findAllByActiveTrue().size() == 0) {
                Role adminRole = this.roleRepository.findByName("ROLE_ADMIN");
                user.addRole(adminRole);
            }
            this.userRepository.saveAndFlush(user);
            return "redirect:/login";
        } catch (IOException e) {
            return "redirect:/error/404";
        }
    }

    @Override
    public String loadLoginPage(Model model) {
        model.addAttribute("view", "user/login");
        return "base-layout";
    }

    @Override
    public String loadLogoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @Override
    public String loadProfilePage(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = this.userRepository.findByEmail(principal.getUsername());
        List<Role> roles = this.roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("view", "user/profile");
        return "base-layout";
    }

    @Override
    public String editProfilePage(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = this.userRepository.findByEmail(principal.getUsername());
        List<Role> roles = this.roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("view", "user/edit");
        return "base-layout";
    }

    @Override
    public String deleteProfilePage(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = this.userRepository.findByEmail(principal.getUsername());
        List<Role> roles = this.roleRepository.findAll();
        Integer numberOfMemes = user.getMemes().size();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("numberOfMemes", numberOfMemes);
        model.addAttribute("view", "user/delete");
        return "base-layout";
    }

    @Override
    public String editProfile(UserBindingModel userBindingModel) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = this.userRepository.findByEmail(principal.getUsername());

        if(!StringUtils.isEmpty(userBindingModel.getPassword())
                && !StringUtils.isEmpty(userBindingModel.getConfirmPassword())) {
            if (userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){

                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                user.setPassword(bCryptPasswordEncoder.encode(userBindingModel.getPassword()));
            }
        }
        user.setFullName(userBindingModel.getFullName());
        user.setEmail(userBindingModel.getEmail());

        this.userRepository.saveAndFlush(user);
        return "redirect:/profile";
    }

    @Override
    public String deleteProfile(String memesDelete) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = this.userRepository.findByEmail(principal.getUsername());
        user.setActive(false);
        if(memesDelete!=null && memesDelete.equals("DELETE")){
            for(Meme meme : user.getMemes()){
                meme.setActive(false);
            }
        }
        this.userRepository.saveAndFlush(user);
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        return "redirect:/";
    }
}
