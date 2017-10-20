package firstblog.blog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "categories")
public class Category {

    private Integer id;

    private String name;

    @JsonBackReference
    private Set<Meme> memes;

    @JsonBackReference
    private boolean active;

    public Category(String name) {
        this.name = name;
        this.memes = new HashSet<>();

        this.active = true;
    }

    public Category() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "category")
    public Set<Meme> getMemes() {
        return memes;
    }

    public void setMemes(Set<Meme> memes) {
        this.memes = memes;
    }

    @JsonBackReference
    @Transient
    public Set<Meme> getActiveMemes(){
        Set<Meme> activeMemes;
        activeMemes = this.getMemes().stream().filter(meme -> meme.isActive()).collect(Collectors.toSet());
        return activeMemes;
    }

    @Column(name = "active", nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
