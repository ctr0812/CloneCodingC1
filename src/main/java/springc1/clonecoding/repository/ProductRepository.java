package springc1.clonecoding.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import springc1.clonecoding.domain.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByIdLessThan(Long lastArticleId, PageRequest pageRequest);
    Page<Product> findByIdLessThanAndLocation(Long lastArticleId ,String location, PageRequest pageRequest);


    List<Product> findAllByOrderByCreatedAtDesc();
    List<Product> findAllByLocationOrderByIdDesc(String location);

}
