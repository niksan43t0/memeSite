package firstblog.blog.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "memes")
public class Meme{

    private Integer id;

    private String title;

    /*private String content;*/

    private User author;

    @JsonBackReference
    private boolean active;

    private Category category;

    @JsonBackReference
    private Set<User> userLikes;

    @JsonBackReference
    private Set<Tag> tags;

    @JsonBackReference
    private Set<Comment> comments;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*@Column(columnDefinition = "text", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }*/
    /*    @Transient
    public String getSummary(){
        if(this.getContent().length()>150){
            return this.getContent().substring(0,150) + "...";
        }
        return this.getContent();
    }*/

    @ManyToOne()
    @JoinColumn(nullable = false, name = "authorId")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Column(name = "active", nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @ManyToOne
    @JoinColumn(nullable = false, name = "categoryId")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToMany()
    @JoinColumn(table = "memes_tags")
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @ManyToMany()
    @JoinTable(name = "users_memes_likes")
    public Set<User> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(Set<User> userLikes) {
        this.userLikes = userLikes;
    }

    @OneToMany(mappedBy = "meme")
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Meme(String title, Category category, HashSet<Tag> tags, User author){
        this.title = title;
        this.author = author;
        this.category = category;
        this.tags = tags;

        this.comments = new HashSet<>();
        this.userLikes = new HashSet<>();
        this.active = true;
    }

    public Meme() {
    }

    @Transient
    public List<Comment> getFilteredComments(Integer page, Integer commentsPerPage){
        List<Comment> comments = this.comments.stream().filter(Comment::isActive).sorted(Comparator.comparingInt(Comment::getId).reversed())
                .collect(Collectors.toList());
        if(page == null ||
                page > (comments.size()/commentsPerPage + 1) ||
                page <1)
            page = 1;

        page = page - 1;
        comments = comments.subList(page*commentsPerPage, Math.min((page+1)*commentsPerPage, comments.size()));
        return comments;
    }
}
