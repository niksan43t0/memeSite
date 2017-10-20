package firstblog.blog.controller;

import firstblog.blog.DTOs.GetCommentsDTO;
import firstblog.blog.DTOs.PostCommentDTO;
import firstblog.blog.entity.Meme;
import firstblog.blog.service.MemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MemeRestController {

    private final MemeService memeService;

    @Autowired
    public MemeRestController(MemeService memeService) {
        this.memeService = memeService;
    }

    @GetMapping("/memes/getAll")
    public List<Meme> listMemes(){
        return memeService.getAllMemes();
    }

    @GetMapping("/memes/page/{page}")
    public List<Meme> listMemes(@PathVariable("page") Integer page){
        return memeService.getMemesPageable(page);
    }

    @GetMapping("/memes/category/{categoryId}/page/{page}")
    public List<Meme> listMemesByCategory(@PathVariable("categoryId") Integer categoryId, @PathVariable("page") Integer page){
        return memeService.getMemesByCategoryPageable(categoryId, page);
    }

    @GetMapping("/memes/tag/{tagName}/page/{page}")
    public List<Meme> listMemesByTag(@PathVariable("tagName") String tagName, @PathVariable("page") Integer page){
        return memeService.getMemesByTagPageable(tagName, page);
    }

    @GetMapping("/meme/{memeId}/like")
    public Integer likeMeme(@PathVariable("memeId") Integer memeId){
        return memeService.likeMeme(memeId);
    }

    @GetMapping("/meme/{memeId}/comments/{page}")
    public GetCommentsDTO listComments(@PathVariable("memeId") Integer memeId,
                                       @PathVariable("page") Integer page){
        return memeService.loadMemeComments(memeId, page);
    }

    @PostMapping("/meme/postComment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> postComment(@Valid @RequestBody PostCommentDTO postCommentDTO){
        if(this.memeService.postComment(postCommentDTO))
            return ResponseEntity.ok(true);
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/meme/deleteComment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteComment(@Valid @RequestBody Integer commentId){
        this.memeService.deleteComment(commentId);
        return ResponseEntity.ok(true);
    }


}
