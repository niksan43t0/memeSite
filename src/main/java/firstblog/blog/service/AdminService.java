package firstblog.blog.service;

import firstblog.blog.bindingModel.UserEditBindingModel;
import org.springframework.ui.Model;

public interface AdminService {
    String loadUserList(Model model);
    String loadEditPage(Model model, Integer userId);
    String editUser(UserEditBindingModel userEditBindingModel, Integer userId);
    String loadDeletePage(Model model, Integer userId);
    String deleteUser(Integer userId, String memesDelete);
}
