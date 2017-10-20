package firstblog.blog.controller;

import firstblog.blog.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    private HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return homeService.loadHomePage(model);
    }

    @GetMapping("/{categoryId}")
    public String showMemesByCategory(Model model, @PathVariable("categoryId") Integer categoryId) {
        return homeService.loadCategoryPage(model, categoryId);
    }

    @GetMapping("/tag/{name}")
    public String memesWithTag(Model model, @PathVariable String name){
        return homeService.loadTagPage(model, name);
    }

    @GetMapping("/tag/")
    public String emptyTag(Model model){
        return homeService.loadTagPage(model, null);
    }

    @GetMapping("/error/{errorMessage}")
    public String errors(Model model, @PathVariable("errorMessage") String errorMessage) {
        return homeService.loadErrorPage(model, errorMessage);
    }
}
