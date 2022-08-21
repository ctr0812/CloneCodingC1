package springc1.clonecoding.controller.response.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import springc1.clonecoding.domain.Product;



@Getter
@NoArgsConstructor
public class ProductAllResponseDto extends ProductDto {

    public ProductAllResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.location = product.getLocation();
    }
}
