package springc1.clonecoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springc1.clonecoding.domain.ImgProduct;
import springc1.clonecoding.domain.Product;

import java.util.List;

public interface ImgProductRepository extends JpaRepository<ImgProduct,Long> {
    List<ImgProduct> findAllByProduct(Product product);
}
