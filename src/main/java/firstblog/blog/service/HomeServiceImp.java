package firstblog.blog.service;

import firstblog.blog.entity.Category;
import firstblog.blog.entity.Tag;
import firstblog.blog.repository.CategoryRepository;
import firstblog.blog.repository.MemeRepository;
import firstblog.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeServiceImp implements HomeService {

    private final List<String> errorList;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    @Autowired
    public HomeServiceImp(CategoryRepository categoryRepository, MemeRepository memeRepository, TagRepository tagRepository) {
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.errorList = new ArrayList<>();
        errorList.add("access");
        errorList.add("email");
        errorList.add("passwordMatch");
    }

    @Override
    public String loadHomePage(Model model) {
        addCategoriesToModel(model);
        return "index2.0";
    }

    @Override
    public String loadCategoryPage(Model model, Integer categoryId) {
        if(!this.categoryRepository.exists(categoryId))
            return "redirect:/";
        Category category = this.categoryRepository.getOne(categoryId);
        addCategoriesToModel(model);
        model.addAttribute("category", category);
        model.addAttribute("view", "tag/categoryTagPage");
        return "base-layout";
    }

    @Override
    public String loadTagPage(Model model, String name) {
        Tag tag = this.tagRepository.findByName(name);
        if(tag == null)
            return "redirect:/";
        addCategoriesToModel(model);
        model.addAttribute("tag", tag);
        model.addAttribute("view", "tag/categoryTagPage");
        return "base-layout";
    }

    @Override
    public String loadErrorPage(Model model, String errorMessage) {
        model.addAttribute("view", "error/errorPage");
        if(!this.errorList.contains(errorMessage))
            errorMessage = "404";
        model.addAttribute("errorMessage", errorMessage);
        return "base-layout";
    }

    private void addCategoriesToModel(Model model) {
        List<Category> categories = this.categoryRepository.findAllByActiveTrue();
        categories = categories.stream().sorted(Comparator.comparingInt(Category::getId)).collect(Collectors.toList());
        model.addAttribute("categories", categories);
        model.addAttribute("showCategories", true);
    }
}
