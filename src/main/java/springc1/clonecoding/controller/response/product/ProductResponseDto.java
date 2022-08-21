package springc1.clonecoding.controller.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springc1.clonecoding.controller.response.ImgResponseDto;
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
public class ProductResponseDto extends ProductDto {


    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.nickname = product.getMember().getNickname();
        this.name = product.getName();
        this.price = product.getPrice();
        this.content = product.getContent();
        this.location = product.getLocation();
        this.createdAt = product.getCreatedAt();
    }
}
