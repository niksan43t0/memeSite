package firstblog.blog.controller.admin;

import firstblog.blog.bindingModel.CategoryBindingModel;
import firstblog.blog.entity.Category;
import firstblog.blog.entity.Meme;
import firstblog.blog.repository.CategoryRepository;
import firstblog.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin/categories/")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String listCategories(Model model){
        return categoryService.loadCategoryList(model);
    }

    @GetMapping("/create")
    public String create(Model model){
        return categoryService.loadCreatePage(model);
    }

    @PostMapping("/create")
    public String createProcess(CategoryBindingModel categoryBindingModel){
        return categoryService.createCategory(categoryBindingModel);
    }

    @GetMapping("/edit/{categoryId}")
    public String edit(Model model, @PathVariable("categoryId") Integer categoryId){
        return categoryService.loadEditPage(model, categoryId);
    }

    @PostMapping("/edit/{categoryId}")
    public String editProcess(CategoryBindingModel categoryBindingModel, @PathVariable("categoryId") Integer categoryId){
        return categoryService.editCategory(categoryBindingModel, categoryId);
    }

    @GetMapping("/delete/{categoryId}")
    public String delete(Model model, @PathVariable("categoryId") Integer categoryId){
        return categoryService.loadDeletePage(model, categoryId);
    }

    @PostMapping("/delete/{categoryId}")
    public String deleteProcess(@PathVariable("categoryId") Integer categoryId){
        return categoryService.deleteCategory(categoryId);
    }

}
