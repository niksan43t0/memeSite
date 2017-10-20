package firstblog.blog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.util.Base64Utils;

import javax.persistence.*;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User{

    private Integer id;

    private String fullName;

    @JsonBackReference
    private String email;

    @JsonBackReference
    private String password;

    @JsonBackReference
    private Set<Role> roles;

    @JsonBackReference
    private Set<Meme> memes;

    @JsonBackReference
    private boolean active;

    @JsonBackReference
    private Set<Comment> comments;

    @JsonBackReference
    private Set<Meme> likedMemes;

    @JsonBackReference
    private byte[] picture;

    public User(String email, String fullName, String password, byte[] picture) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.picture = picture;

        this.roles = new HashSet<>();
        this.memes = new HashSet<>();
        this.comments = new HashSet<>();
        this.likedMemes = new HashSet<>();
        this.active = true;
    }

    public User(){
    }

    public void addRole(Role role){
        this.roles.add(role);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(length = 60, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles")
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @OneToMany(mappedBy = "author")
    public Set<Meme> getMemes() {
        return memes.stream().filter(meme -> meme.isActive()).collect(Collectors.toSet());
    }

    public void setMemes(Set<Meme> memes) {
        this.memes = memes;
    }

    @ManyToMany(mappedBy = "userLikes")
    public Set<Meme> getLikedMemes() {
        return likedMemes;
    }

    public void setLikedMemes(Set<Meme> likedMemes) {
        this.likedMemes = likedMemes;
    }

    @Column(name = "active", nullable = false)
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    @OneToMany(mappedBy = "author")
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Column(name="picture")
    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @JsonBackReference
    @Transient
    public String getEncodedPicture() {
        if(this.picture!=null)
            return Base64.getEncoder().encodeToString(this.picture);
        return null;
    }

    @Transient
    public boolean isAdmin(){
        return this.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    @Transient
    public boolean isAuthor(Meme meme){
        return Objects.equals(this.getId(), meme.getAuthor().getId());
    }

    @Transient
    public boolean isAuthor(Comment comment){
        return Objects.equals(this.getId(), comment.getAuthor().getId());
    }
}



