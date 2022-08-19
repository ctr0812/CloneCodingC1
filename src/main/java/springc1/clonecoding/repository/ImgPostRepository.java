package springc1.clonecoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springc1.clonecoding.domain.ImgPost;
import springc1.clonecoding.domain.ImgProduct;
import springc1.clonecoding.domain.Post;
import springc1.clonecoding.domain.Product;

import java.util.List;

public interface ImgPostRepository extends JpaRepository<ImgPost,Long> {
    List<ImgPost> findAllByPost(Post post);
}
