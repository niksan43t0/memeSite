package firstblog.blog.service;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.SeekableStream;
import firstblog.blog.DTOs.GetCommentsDTO;
import firstblog.blog.DTOs.PostCommentDTO;
import firstblog.blog.bindingModel.MemeBindingModel;
import firstblog.blog.entity.*;
import firstblog.blog.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemeServiceImp implements MemeService {

    private final MemeRepository memeRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    private final CommentRepository commentRepository;

    @Autowired
    public MemeServiceImp(MemeRepository memeRepository, UserRepository userRepository, CategoryRepository categoryRepository, TagRepository tagRepository, CommentRepository commentRepository) {
        this.memeRepository = memeRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public String loadCreatePage(Model model) {
        List<Category> categories = this.categoryRepository.findAllByActiveTrue();
        model.addAttribute("categories", categories);
        model.addAttribute("view", "meme/create");
        return "base-layout";
    }

    @Override
    public String createMeme(MemeBindingModel memeBindingModel) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());

        Category category = this.categoryRepository.findOne(memeBindingModel.getCategoryId());
        HashSet<Tag> tags = findTagsFromString(memeBindingModel.getTagString());

        MultipartFile image = memeBindingModel.getMemeImage();
        List<String> okContentTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg");
        if (!okContentTypes.contains(image.getContentType())) {
            return "redirect:/meme/create";
        }
        Meme memeEntity = new Meme(memeBindingModel.getTitle(),
                category,
                tags,
                userEntity);
        this.memeRepository.saveAndFlush(memeEntity);
        String staticFolderPath = getImagePath(memeBindingModel);
        saveMemeImage(memeEntity, image, staticFolderPath);

        return "redirect:/meme/" + memeEntity.getId();
    }

    @Override
    public String loadMeme(Model model, Integer memeId) {
        if (!this.memeRepository.exists(memeId))
            return "redirect:/";
        Meme meme = this.memeRepository.getOne(memeId);
        if(!meme.isActive()) {
            return "redirect:/";
        }

        if(!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User userEntity = this.userRepository.findByEmail(user.getUsername());
            model.addAttribute("user", userEntity);
        }else
            model.addAttribute("user", null);
        model.addAttribute("meme", meme);
        model.addAttribute("view", "meme/details");
        return "base-layout";
    }

    @Override
    public GetCommentsDTO loadMemeComments(Integer memeId, Integer commentPage) {
        commentPage--;
        Meme meme = this.memeRepository.getOne(memeId);
        //get max commentPage
        Integer maxCommentPage;
        Integer commentsCount = this.commentRepository.countAllByMemeAndActiveTrue(meme);
        if(commentsCount % 5 == 0)
            maxCommentPage = commentsCount / 5 - 1;
        else
            maxCommentPage = commentsCount / 5;

        if(commentPage < 0 || commentPage > maxCommentPage)
            commentPage = maxCommentPage;

        PageRequest request = new PageRequest(Math.max(commentPage, 0) , 5);
        Page<Comment> comments = this.commentRepository.findAllByMemeAndActiveTrueOrderByIdAsc(meme, request);

        if(!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User userEntity = this.userRepository.findByEmail(user.getUsername());
            boolean isUserAuthorOrAdmin = isUserAuthorOrAdmin(this.memeRepository.getOne(memeId));
            Integer userId = userEntity.getId();
            return new GetCommentsDTO(comments, isUserAuthorOrAdmin, userId);
        }

        return new GetCommentsDTO(comments, false, null);
    }

    @Override
    public String loadEditPage(Model model, Integer memeId) {
        if(!this.memeRepository.exists(memeId))
            return "redirect:/";

        Meme meme = this.memeRepository.getOne(memeId);
        if(!meme.isActive()) {
            return "redirect:/";
        }
        if(!isUserAuthorOrAdmin(meme)) {
            return "redirect:/meme/" + memeId;
        }
        List<Category> categories = this.categoryRepository.findAllByActiveTrue();
        String tagString = meme.getTags().stream().map(Tag::getName).collect(Collectors.joining(", "));

        model.addAttribute("meme", meme);
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tagString);
        model.addAttribute("view", "meme/edit");
        return "base-layout";
    }

    @Override
    public String editMeme(MemeBindingModel memeBindingModel, Integer memeId) {
        if (!this.memeRepository.exists(memeId))
            return "redirect:/";
        Meme meme = this.memeRepository.getOne(memeId);
        if(!meme.isActive()) {
            return "redirect:/";
        }
        if(!isUserAuthorOrAdmin(meme)) {
            return "redirect:/meme/" + memeId;
        }
        Category category = this.categoryRepository.findOne(memeBindingModel.getCategoryId());
        HashSet<Tag> tags = findTagsFromString(memeBindingModel.getTagString());

        meme.setTitle(memeBindingModel.getTitle());
        meme.setCategory(category);
        meme.setTags(tags);
        this.memeRepository.saveAndFlush(meme);
        return "redirect:/meme/" + meme.getId();
    }

    @Override
    public String loadDeletePage(Model model, Integer memeId) {
        if(!this.memeRepository.exists(memeId))
            return "redirect:/";
        Meme meme = this.memeRepository.getOne(memeId);
        if(!meme.isActive())
            return "redirect:/";
        if(!isUserAuthorOrAdmin(meme))
            return "redirect:/meme/" + memeId;
        model.addAttribute("meme", meme);

        model.addAttribute("view", "meme/delete");
        return "base-layout";
    }

    @Override
    public String deleteMeme(Integer memeId) {
        if(!this.memeRepository.exists(memeId))
            return "redirect:/";
        Meme meme = this.memeRepository.getOne(memeId);
        if(!meme.isActive()) {
            return "redirect:/";
        }
        if(!isUserAuthorOrAdmin(meme)) {
            return "redirect:/meme/" + memeId;
        }
        meme.setActive(false);
        this.memeRepository.saveAndFlush(meme);
        return "redirect:/";
    }

    @Override
    public boolean postComment(PostCommentDTO postCommentDTO) {
        if(postCommentDTO.getCommentContent().isEmpty())
            return false;
        Meme meme = this.memeRepository.getOne(postCommentDTO.getMemeId());
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());
        Comment comment = new Comment(postCommentDTO.getCommentContent(), userEntity, meme);
        this.commentRepository.saveAndFlush(comment);
        return true;
    }

    @Override
    public void deleteComment(Integer commentId) {
        if(!this.commentRepository.exists(commentId))
            return;
        Comment comment = this.commentRepository.getOne(commentId);
        if(!comment.isActive()) {
            return;
        }
        if(!isUserAuthorOrMemeAuthorOrAdmin(comment)) {
            return;
        }
        comment.setActive(false);
        this.commentRepository.saveAndFlush(comment);
    }

    @Override
    public List<Meme> getAllMemes() {
        return memeRepository.findAllByActiveTrueOrderByIdDesc();
    }

    @Override
    public List<Meme> getMemesPageable(Integer page) {
        PageRequest request = new PageRequest(page, 3);
        List<Meme> memes = this.memeRepository.findAllByActiveTrueOrderByIdDesc(request);
        return memes;
    }

    @Override
    public List<Meme> getMemesByCategoryPageable(Integer categoryId, Integer page) {
        PageRequest request = new PageRequest(page, 3);
        return this.memeRepository.findAllByCategoryAndActiveTrueOrderByIdDesc(this.categoryRepository.getOne(categoryId) ,request);
    }

    @Override
    public List<Meme> getMemesByTagPageable(String tagName, Integer page) {
        PageRequest request = new PageRequest(page, 3);
        return this.memeRepository.findAllByTagsAndActiveTrueOrderByIdDesc(this.tagRepository.findByName(tagName) ,request);
    }

    @Override
    public Integer likeMeme(Integer memeId) {
        if (!this.memeRepository.exists(memeId))
            return null;
        Meme meme = this.memeRepository.getOne(memeId);
        if(!meme.isActive()) {
            return null;
        }
        UserDetails user;
        try{ user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception e){
            return null;
        }
        User userEntity = this.userRepository.findByEmail(user.getUsername());
        if(meme.getUserLikes().contains(userEntity)){
            meme.getUserLikes().remove(userEntity);
        }else{
            meme.getUserLikes().add(userEntity);
        }
        this.memeRepository.saveAndFlush(meme);
        return meme.getUserLikes().size();
    }

    private HashSet<Tag> findTagsFromString(String tagString) {
        HashSet<Tag> tags = new HashSet<>();
        String[] tagNames = tagString.toLowerCase().split(",\\s*");
        for (String tagName : tagNames) {
            Tag currentTag = this.tagRepository.findByName(tagName);
            if (currentTag == null) {
                currentTag = new Tag(tagName);
                tagRepository.saveAndFlush(currentTag);
            }
            tags.add(currentTag);
        }
        return tags;
    }

    private String getImagePath(MemeBindingModel memeBindingModel){
        URL jsonPackageUrl = memeBindingModel.getClass().getClassLoader().getResource("static/package.json");
        String staticFolderPath = jsonPackageUrl.getPath();
        int endIndex = staticFolderPath.indexOf("package.json");
        staticFolderPath = staticFolderPath.substring(1, endIndex);
        staticFolderPath = staticFolderPath.replace("%20", " ");
        System.out.println(staticFolderPath + "img/memeImages");
        System.out.println(new File(staticFolderPath + "img/memeImages").mkdirs());

        staticFolderPath = staticFolderPath.replace("/", "\\");
        return staticFolderPath;
    }

    private void saveMemeImage(Meme meme, MultipartFile multipartFileImage, String staticFolderPath){
        String imageName = meme.getId().toString();
        String destination = staticFolderPath + "img\\memeImages\\" + imageName + ".jpg";
        try{
            byte[] source = multipartFileImage.getBytes();
            SeekableStream inputStream = new ByteArraySeekableStream(source);
            RenderedOp image = JAI.create("stream", inputStream);
            JAI.create("filestore",image,destination,"JPEG"); // Destination directory must exist (D:\someFolder\)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isUserAuthorOrAdmin(Meme meme){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin()||userEntity.isAuthor(meme);
    }

    private boolean isUserAuthorOrMemeAuthorOrAdmin(Comment comment){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin()|| userEntity.isAuthor(comment.getMeme())||userEntity.isAuthor(comment) ;
    }
}
