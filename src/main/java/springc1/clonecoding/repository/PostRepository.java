package springc1.clonecoding.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import springc1.clonecoding.domain.Post;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByLocation(String location);
}
