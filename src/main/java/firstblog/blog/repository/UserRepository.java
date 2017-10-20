package firstblog.blog.repository;

import firstblog.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(Integer id);
    User findByEmail(String email);
    List<User> findAllByActiveTrue();
}
