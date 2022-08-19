package springc1.clonecoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springc1.clonecoding.domain.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByLocation(String location);
}
