package firstblog.blog.bindingModel;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class MemeBindingModel {

    @NotNull
    private String title;
    /*@NotNull
    private String content;*/

    @NotNull
    private MultipartFile memeImage;

    @NotNull
    private Integer categoryId;

    private String tagString;


    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

/*    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }*/

    public MultipartFile getMemeImage() {
        return memeImage;
    }

    public void setMemeImage(MultipartFile memeImage) {
        this.memeImage = memeImage;
    }
}
