package firstblog.blog.controller;

import firstblog.blog.bindingModel.MemeBindingModel;
import firstblog.blog.service.MemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemeController {

    private final MemeService memeService;

    @Autowired
    public MemeController(MemeService memeService) {
        this.memeService = memeService;
    }

    @GetMapping("/meme/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model) {
        return memeService.loadCreatePage(model);
    }


    @PostMapping("/meme/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(MemeBindingModel memeBindingModel) {
       return memeService.createMeme(memeBindingModel);
    }

    @GetMapping("meme/{memeId}")
    public String details(@PathVariable("memeId") Integer memeId,
                          Model model) {
        return memeService.loadMeme(model, memeId);
    }

    @GetMapping("/meme/edit/{memeId}")
    @PreAuthorize("isAuthenticated()")
    public String edit(Model model, @PathVariable("memeId") Integer memeId){
        return memeService.loadEditPage(model, memeId);
    }

    @PostMapping("/meme/edit/{memeId}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(MemeBindingModel memeBindingModel, @PathVariable("memeId") Integer memeId) {
        return memeService.editMeme(memeBindingModel, memeId);
    }

    @GetMapping("/meme/delete/{memeId}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable("memeId") Integer memeId){
        return memeService.loadDeletePage(model, memeId);
    }

    @PostMapping("meme/delete/{memeId}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable("memeId") Integer memeId){
        return memeService.deleteMeme(memeId);
    }

}
