package firstblog.blog.service;


import firstblog.blog.bindingModel.UserBindingModel;
import firstblog.blog.bindingModel.UserEditBindingModel;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    String loadRegisterPage(Model model);
    String registerUser(UserBindingModel userBindingModel);
    String loadLoginPage(Model model);
    String loadLogoutPage(HttpServletRequest request, HttpServletResponse response);
    String loadProfilePage(Model model);
    String editProfilePage(Model model);
    String deleteProfilePage(Model model);
    String editProfile(UserBindingModel userBindingModel);
    String deleteProfile(String memesDelete);
}
