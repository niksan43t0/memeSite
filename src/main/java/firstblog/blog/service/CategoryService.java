package firstblog.blog.service;

import firstblog.blog.bindingModel.CategoryBindingModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

public interface CategoryService {
    String loadCategoryList(Model model);
    String loadCreatePage(Model model);
    String createCategory(CategoryBindingModel categoryBindingModel);
    String loadEditPage(Model model, Integer categoryId);
    String editCategory(CategoryBindingModel categoryBindingModel,  Integer categoryId);
    String loadDeletePage(Model model, Integer categoryId);
    String deleteCategory(Integer categoryId);
}


