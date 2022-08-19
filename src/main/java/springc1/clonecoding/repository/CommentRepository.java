package springc1.clonecoding.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import springc1.clonecoding.domain.Comment;
import springc1.clonecoding.domain.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}