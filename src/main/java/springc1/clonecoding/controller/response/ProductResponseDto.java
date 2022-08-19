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
public class ProductResponseDto {

    private Long id;
    private String nickname;
    private String name;
    private Long price;
    private String content;
    private String location;
    private List<ImgResponseDto> imgProductList = new ArrayList<>();
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
