package springc1.clonecoding.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springc1.clonecoding.domain.ImgProduct;
import springc1.clonecoding.domain.Member;
import springc1.clonecoding.domain.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAllResponseDto {

    private Long id;
    private String name;
    private Long price;
    private String location;
    private List<ImgResponseDto> imgProductList = new ArrayList<>();

    public ProductAllResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.location = product.getLocation();
    }
}
