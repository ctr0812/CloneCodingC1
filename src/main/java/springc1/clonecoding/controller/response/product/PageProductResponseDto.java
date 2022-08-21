package springc1.clonecoding.controller.response.product;

import lombok.*;
import springc1.clonecoding.controller.response.ImgResponseDto;
import springc1.clonecoding.domain.Product;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageProductResponseDto {
    private Long id;
    private String name;
    private Long price;
    private String location;
    private List<ImgResponseDto> imgProductList = new ArrayList<>();

    public PageProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.location = product.getLocation();
    }
}
