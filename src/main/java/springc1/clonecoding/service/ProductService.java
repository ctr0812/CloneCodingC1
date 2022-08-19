package springc1.clonecoding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.clonecoding.controller.request.ProductRequestDto;
import springc1.clonecoding.controller.response.ImgResponseDto;
import springc1.clonecoding.controller.response.ProductAllResponseDto;
import springc1.clonecoding.controller.response.ProductResponseDto;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.domain.*;
import springc1.clonecoding.repository.ImgProductRepository;
import springc1.clonecoding.repository.ProductRepository;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ImgProductRepository imgProductRepository;

    public ResponseDto<?> createProduct(ProductRequestDto requestDto , UserDetailsImpl userDetails) {
         // 로그인된 멤버 정보 가져오기
        Member member = userDetails.getMember();
         // 새로운 상품 생성
        Product product = new Product(requestDto, member);
         // db에 product 저장
        productRepository.save(product);

        // 이미지 저장
        List<ImgProduct> imgProductList = requestDto.getImgProductList();
        for (ImgProduct imgProduct : imgProductList) {
            imgProduct.setProduct(product);
            imgProductRepository.save(imgProduct);
        }


        return ResponseDto.success("success");
    }

    public ResponseDto<?> updateProduct(Long id, ProductRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
        // id 로 상품게시글 존재 유무 확인
        Product product = isPresentProduct(id);
        // 상품게시글 작성자만이 수정 ,삭제 가능
        memberValidateProduct(member, product);
        // product 업데이트
        product.update(requestDto);
        return ResponseDto.success("success");
    }

    public ResponseDto<?> deleteProduct(Long id, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
        // id 로 상품게시글 존재 유무 확인
        Product product = isPresentProduct(id);
        // 상품게시글 작성자만이 수정 ,삭제 가능
        memberValidateProduct(member, product);
        productRepository.delete(product);
        return ResponseDto.success("success");

    }

    public ResponseDto<?> getLocationProduct(String location) {
        List<Product> productList = productRepository.findAllByLocation(location);
        List<ProductAllResponseDto> products = new ArrayList<>();

        for(Product product : productList){
            ProductAllResponseDto responseDto = new ProductAllResponseDto(product);
            List<ImgProduct> imgProductList = imgProductRepository.findAllByProduct(product);
            for(ImgProduct imgProduct : imgProductList){
                String imgUrl = imgProduct.getImgUrl();
                ImgResponseDto imgResponseDto = new ImgResponseDto(imgUrl);
                responseDto.getImgProductList().add(imgResponseDto);
            }
            products.add(responseDto);
        }
        return ResponseDto.success(products);
    }

    public ResponseDto<?> getAllProduct() {
        List<Product> productList = productRepository.findAll();
        List<ProductAllResponseDto> products = new ArrayList<>();

        for(Product product : productList){
            ProductAllResponseDto responseDto = new ProductAllResponseDto(product);
            List<ImgProduct> imgProductList = imgProductRepository.findAllByProduct(product);
            for(ImgProduct imgProduct : imgProductList){
                String imgUrl = imgProduct.getImgUrl();
                ImgResponseDto imgResponseDto = new ImgResponseDto(imgUrl);
                responseDto.getImgProductList().add(imgResponseDto);
            }
            products.add(responseDto);
        }
        return ResponseDto.success(products);
    }


    public ResponseDto<?> getProduct(Long id , UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        // id 로 상품게시글 존재 유무 확인
        Product product = isPresentProduct(id);
        return ResponseDto.success(new ProductResponseDto(member, product));
    }


    // id 로 상품 존재 유무 확인
    @Transactional(readOnly = true)
    public Product isPresentProduct(Long id) {

        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product= optionalProduct.orElse(null);

        if (null == product) {
            throw new IllegalArgumentException("존재하지 않는 상품 id 입니다");
        } else{
            return product;
        }
    }

    // 상품 작성자만이 수정 ,삭제 가능
    @Transactional(readOnly = true)
    public void memberValidateProduct(Member member, Product product) {
        if (product.validateMember(member)) {
            throw new IllegalArgumentException("상품 게시글 작성자가 아닙니다");
        }
    }

}
