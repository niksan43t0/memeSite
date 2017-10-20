package firstblog.blog.service;

import firstblog.blog.DTOs.GetCommentsDTO;
import firstblog.blog.DTOs.PostCommentDTO;
import firstblog.blog.bindingModel.MemeBindingModel;
import firstblog.blog.entity.Meme;
import org.springframework.ui.Model;

import java.util.List;

public interface MemeService {
    String loadCreatePage(Model model);
    String createMeme(MemeBindingModel memeBindingModel);
    String loadMeme(Model model, Integer memeId);
    GetCommentsDTO loadMemeComments(Integer memeId, Integer commentPage);
    String loadEditPage(Model model, Integer memeId);
    String editMeme(MemeBindingModel memeBindingModel, Integer memeId);
    String loadDeletePage(Model model, Integer memeId);
    String deleteMeme(Integer memeId);
    boolean postComment(PostCommentDTO postCommentDTO);
    void deleteComment(Integer commentId);
    List<Meme> getAllMemes();
    List<Meme> getMemesPageable(Integer page);
    List<Meme> getMemesByCategoryPageable(Integer categoryId, Integer page);
    List<Meme> getMemesByTagPageable(String tagName, Integer page);
    Integer likeMeme(Integer memeId);

}
