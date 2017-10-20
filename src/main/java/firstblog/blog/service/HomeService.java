package firstblog.blog.service;

import org.springframework.ui.Model;

public interface HomeService {
    String loadHomePage(Model model);
    String loadCategoryPage(Model model, Integer categoryId);
    String loadErrorPage(Model model, String errorMessage);
    String loadTagPage(Model model, String name);
}
