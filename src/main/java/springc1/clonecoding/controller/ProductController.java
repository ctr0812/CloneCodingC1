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

    @PostMapping(value = "/api/product")
    public ResponseDto<?> createProduct(@RequestBody ProductRequestDto productRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return productService.createProduct(productRequestDto, userDetails);
    }

    @PutMapping(value = "/api/product/{id}")
    public ResponseDto<?> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return productService.updateProduct(id, productRequestDto, userDetails);
    }


    @DeleteMapping(value = "/api/product{id}")
    public ResponseDto<?> deleteProduct(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return productService.deleteProduct(id, userDetails);
    }


    @GetMapping(value = "/api/product/{location}")
    public ResponseDto<?> getLocationProduct(@PathVariable String location){
        return productService.getLocationProduct(location);
    }

    @GetMapping(value = "/api/product")
    public ResponseDto<?> getAllProduct(){
        return productService.getAllProduct();
    }

    @GetMapping(value = "/api/product/id/{id}")
    public ResponseDto<?> getProduct(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return productService.getProduct(id, userDetails);
    }
}
