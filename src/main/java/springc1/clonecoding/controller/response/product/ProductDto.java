package springc1.clonecoding.controller.response.product;

import lombok.Getter;
import lombok.Setter;
import springc1.clonecoding.controller.response.ImgResponseDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductDto {
    protected Long id;
    protected String name;
    protected Long price;
    protected String location;
    protected List<ImgResponseDto> imgProductList = new ArrayList<>();
}
