package firstblog.blog.service;

import firstblog.blog.bindingModel.UserEditBindingModel;
import firstblog.blog.entity.Meme;
import firstblog.blog.entity.Role;
import firstblog.blog.entity.User;
import firstblog.blog.repository.RoleRepository;
import firstblog.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminServiceImp implements AdminService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public AdminServiceImp(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public String loadUserList(Model model) {
        List<User> users = this.userRepository.findAllByActiveTrue();
        users = users.stream().sorted(Comparator.comparingInt(User::getId)).collect(Collectors.toList());

        model.addAttribute("users", users);
        model.addAttribute("view", "admin/user/list");
        return "base-layout";
    }

    @Override
    public String loadEditPage(Model model, Integer userId) {

        if(!this.userRepository.exists(userId)) {
            return "redirect:/admin/users/";
        }
        User user = this.userRepository.findOne(userId);

        if(!user.isActive()) {
            return "redirect:/admin/users/";
        }
        List<Role> roles = this.roleRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("view", "admin/user/edit");
        return "base-layout";
    }

    @Override
    public String editUser(UserEditBindingModel userEditBindingModel, Integer userId) {
        if(!this.userRepository.exists(userId)) {
            return "redirect:/admin/users/";
        }
        User user = this.userRepository.getOne(userId);
        if(!user.isActive()) {
            return "redirect:/admin/users/";
        }
        if(!StringUtils.isEmpty(userEditBindingModel.getPassword())
                && !StringUtils.isEmpty(userEditBindingModel.getConfirmPassword())) {
            if (userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())){

                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                user.setPassword(bCryptPasswordEncoder.encode(userEditBindingModel.getPassword()));
            }
        }
        user.setFullName(userEditBindingModel.getFullName());
        user.setEmail(userEditBindingModel.getEmail());

        Set<Role> roles = new HashSet<>();
        for(Integer roleId : userEditBindingModel.getRoles()){
            roles.add(this.roleRepository.findOne(roleId));
        }
        if(roles.isEmpty()) {
            user.setActive(false);
        } else {
            user.setRoles(roles);
        }
        this.userRepository.saveAndFlush(user);
        return "redirect:/admin/users/";
    }

    @Override
    public String loadDeletePage(Model model, Integer userId) {
        if(!this.userRepository.exists(userId)) {
            return "redirect:/admin/users/";
        }
        User user = this.userRepository.getOne(userId);
        if(!user.isActive()) {
            return "redirect:/admin/users/";
        }
        List<Role> roles = this.roleRepository.findAll();
        Integer numberOfMemes = user.getMemes().size();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("numberOfMemes", numberOfMemes);
        model.addAttribute("view", "admin/user/delete");
        return "base-layout";
    }

    @Override
    public String deleteUser(Integer userId, String memesDelete) {
        if(!this.userRepository.exists(userId)) {
            return "redirect:/admin/users/";
        }
        User user = this.userRepository.getOne(userId);
        if(!user.isActive()) {
            return "redirect:/admin/users/";
        }
        user.setActive(false);
        if(memesDelete!=null && memesDelete.equals("DELETE")){
            for(Meme meme : user.getMemes()){
                meme.setActive(false);
            }
        }
        this.userRepository.saveAndFlush(user);
        logOutIfDeletingSelf(user);
        return "redirect:/admin/users/";
    }

    private void logOutIfDeletingSelf(User user){
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUserEntity = this.userRepository.findByEmail(currentUser.getUsername());
        if(user.getId().equals(currentUserEntity.getId()))
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }
}
