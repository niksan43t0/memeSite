package firstblog.blog.controller.admin;

import firstblog.blog.bindingModel.UserEditBindingModel;
import firstblog.blog.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminService adminService;

    @Autowired
    public AdminUserController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String listUsers(Model model){
        return adminService.loadUserList(model);
    }

    @GetMapping("/edit/{userId}")
    public String edit(Model model, @PathVariable("userId") Integer userId){
        return adminService.loadEditPage(model, userId);
    }

    @PostMapping("edit/{userId}")
    public String editProcess(UserEditBindingModel userEditBindingModel, @PathVariable("userId") Integer userId){
        return adminService.editUser(userEditBindingModel, userId);
    }

    @GetMapping("/delete/{userId}")
    public String delete(Model model, @PathVariable("userId") Integer userId){
        return adminService.loadDeletePage(model, userId);
    }

    @PostMapping("delete/{userId}")
    public String deleteProcess(@PathVariable("userId") Integer userId, @RequestParam(value = "memesDelete", required = false) String memesDelete){
        return adminService.deleteUser(userId, memesDelete);
    }

}
