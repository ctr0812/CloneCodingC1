package springc1.clonecoding.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springc1.clonecoding.domain.ImgProduct;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    private String name;
    private Long price;
    private List<ImgProduct> imgProductList;
    private String content;
}
