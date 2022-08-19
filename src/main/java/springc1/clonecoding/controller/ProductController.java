package springc1.clonecoding.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springc1.clonecoding.controller.request.ProductRequestDto;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.domain.UserDetailsImpl;
import springc1.clonecoding.service.ProductService;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    //api 상품 작성
    @PostMapping(value = "/api/product")
    public ResponseDto<?> createProduct(@RequestBody ProductRequestDto productRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return productService.createProduct(productRequestDto, userDetails);
    }

    //api 상품 수정
    @PutMapping(value = "/api/product/{id}")
    public ResponseDto<?> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return productService.updateProduct(id, productRequestDto, userDetails);
    }

    //api 상품 삭제
    @DeleteMapping(value = "/api/product{id}")
    public ResponseDto<?> deleteProduct(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return productService.deleteProduct(id, userDetails);
    }

    //api 특정 지역 상품 전체 조회
    @GetMapping(value = "/api/product/{location}")
    public ResponseDto<?> getLocationProduct(@PathVariable String location){
        return productService.getLocationProduct(location);
    }

    //api 전체 지역 상품 전체 조회
    @GetMapping(value = "/api/product")
    public ResponseDto<?> getAllProduct(){
        return productService.getAllProduct();
    }


    //api id로 상품 상세 조회
    @GetMapping(value = "/api/product/id/{id}")
    public ResponseDto<?> getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }
}
