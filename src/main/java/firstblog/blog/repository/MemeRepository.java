package firstblog.blog.repository;

import firstblog.blog.entity.Category;
import firstblog.blog.entity.Meme;
import firstblog.blog.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemeRepository extends JpaRepository<Meme, Integer>{
    List<Meme> findAllByActiveTrueOrderByIdDesc();

    List<Meme> findAllByActiveTrueOrderByIdDesc(Pageable pageable);

    List<Meme> findAllByCategoryAndActiveTrueOrderByIdDesc(Category category, Pageable pageable);

    List<Meme> findAllByTagsAndActiveTrueOrderByIdDesc(Tag tag, Pageable pageable);
}
