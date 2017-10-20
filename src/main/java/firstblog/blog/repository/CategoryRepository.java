package firstblog.blog.repository;

import firstblog.blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAllByActiveTrue();
    Category findByName(String name);
}
