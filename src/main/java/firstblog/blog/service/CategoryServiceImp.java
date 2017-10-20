package firstblog.blog.service;

import firstblog.blog.bindingModel.CategoryBindingModel;
import firstblog.blog.entity.Category;
import firstblog.blog.entity.Meme;
import firstblog.blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String loadCategoryList(Model model){
        List<Category> categories = this.categoryRepository.findAllByActiveTrue();
        categories = categories.stream().sorted(Comparator.comparingInt(Category::getId))
                .collect(Collectors.toList());
        model.addAttribute("categories", categories);
        model.addAttribute("view", "admin/category/list");
        return "base-layout";
    }

    @Override
    public String loadCreatePage(Model model) {
        model.addAttribute("view", "admin/category/create");
        return "base-layout";
    }

    @Override
    public String createCategory(CategoryBindingModel categoryBindingModel) {
        if(StringUtils.isEmpty(categoryBindingModel.getName()))
            return "redirect:/admin/categories/create";
        if(this.categoryRepository.findByName(categoryBindingModel.getName())!=null)
            return "redirect:/admin/categories/create";

        Category category = new Category(categoryBindingModel.getName());
        this.categoryRepository.saveAndFlush(category);
        return "redirect:/admin/categories/";
    }

    @Override
    public String loadEditPage(Model model, Integer categoryId) {
        if(!this.categoryRepository.exists(categoryId))
            return "redirect:/admin/categories/";
        Category category = this.categoryRepository.getOne(categoryId);
        if(!category.isActive()) {
            return "redirect:/admin/categories/";
        }
        model.addAttribute("category", category);
        model.addAttribute("view", "admin/category/edit");
        return "base-layout";
    }

    @Override
    public String editCategory(CategoryBindingModel categoryBindingModel, Integer categoryId) {
        if(!this.categoryRepository.exists(categoryId))
            return "redirect:/admin/categories/";
        Category category = this.categoryRepository.getOne(categoryId);
        if(!category.isActive()) {
            return "redirect:/admin/categories/";
        }
        category.setName(categoryBindingModel.getName());
        this.categoryRepository.saveAndFlush(category);
        return "redirect:/admin/categories/";
    }

    @Override
    public String loadDeletePage(Model model, Integer categoryId) {
        if(!this.categoryRepository.exists(categoryId))
            return "redirect:/admin/categories/";
        Category category = this.categoryRepository.getOne(categoryId);
        if(!category.isActive()) {
            return "redirect:/admin/categories/";
        }
        model.addAttribute("category", category);
        model.addAttribute("view", "admin/category/delete");
        return "base-layout";
    }

    @Override
    public String deleteCategory(Integer categoryId) {
        if(!this.categoryRepository.exists(categoryId))
            return "redirect:/admin/categories/";
        Category category = this.categoryRepository.getOne(categoryId);
        if(!category.isActive()) {
            return "redirect:/admin/categories/";
        }
        category.setActive(false);
        for(Meme meme:category.getMemes()){
            meme.setActive(false);
        }
        this.categoryRepository.saveAndFlush(category);
        return "redirect:/admin/categories/";
    }
}
