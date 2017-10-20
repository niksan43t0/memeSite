package firstblog.blog.repository;

import firstblog.blog.entity.Comment;
import firstblog.blog.entity.Meme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByMemeAndActiveTrue(Meme meme);
    Page<Comment> findAllByMemeAndActiveTrueOrderByIdAsc(Meme meme, Pageable pageable);
    Integer countAllByMemeAndActiveTrue(Meme meme);
}