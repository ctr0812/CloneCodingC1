package springc1.clonecoding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.clonecoding.controller.request.PageRequestDto;
import springc1.clonecoding.controller.request.ProductRequestDto;
import springc1.clonecoding.controller.response.*;
import springc1.clonecoding.controller.response.product.PageProductResponseDto;
import springc1.clonecoding.controller.response.product.ProductAllResponseDto;
import springc1.clonecoding.controller.response.product.ProductDto;
import springc1.clonecoding.controller.response.product.ProductResponseDto;
import springc1.clonecoding.domain.*;
import springc1.clonecoding.repository.ImgProductRepository;
import springc1.clonecoding.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ImgProductRepository imgProductRepository;

    @Transactional
    public ResponseDto<?> createProduct(ProductRequestDto requestDto , UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Product product = new Product(requestDto, member);

        productRepository.save(product);

        imgSave(requestDto, product);
        return ResponseDto.success("success");
    }


    @Transactional
    public ResponseDto<?> updateProduct(Long id, ProductRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Product product = getPropductById(id);

        productMemberValidate(member, product);

        product.update(requestDto);

        imgProductRepository.deleteAll(imgProductRepository.findAllByProduct(product));
        imgSave(requestDto, product);
        return ResponseDto.success("success");
    }


    @Transactional
    public ResponseDto<?> deleteProduct(Long id, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Product product = getPropductById(id);

        productMemberValidate(member, product);
        productRepository.delete(product);
        return ResponseDto.success("success");

    }


    @Transactional
    public ResponseDto<?> getLocationProduct(String location) {

        List<Product> productList = productRepository.findAllByLocationOrderByIdDesc(location);

        return ResponseDto.success(getProductAllResponseDto(productList));
    }


    @Transactional
    public ResponseDto<?> getAllProduct() {

        List<Product> productList = productRepository.findAllByOrderByCreatedAtDesc();

        return ResponseDto.success(getProductAllResponseDto(productList));
    }


    @Transactional
    public ResponseDto<?> getProduct(Long id) {

        Product product = getPropductById(id);
        ProductResponseDto responseDto = new ProductResponseDto(product);

        getImgResponseDto(product, responseDto);

        return ResponseDto.success(responseDto);


    }

    @Transactional
    public ResponseDto<?> getAllProductPagesBy(PageRequestDto reqeustDto) {

        PageRequest pageRequest = PageRequest.of(0, reqeustDto.getSize(), Sort.by("id").descending());
        return ResponseDto.success(getPageProductResponseDtoList(pageRequest,reqeustDto));

    }

    @Transactional
    public ResponseDto<?> getAllProductLocationPagesBy(PageRequestDto requestDto, String location) {
        PageRequest pageRequest = PageRequest.of(0, requestDto.getSize(),Sort.by("id").descending());
        return ResponseDto.success(getPageLocationResponseDtoList(pageRequest,location,requestDto));
    }



     // 전체 page Dto 반환
    private List<PageProductResponseDto> getPageProductResponseDtoList(PageRequest pageRequest, PageRequestDto requestDto) {
        if (requestDto.getLastArticleId() == null) {
            Page<Product> products = productRepository.findAll(pageRequest);
            return toDtoList(products);
        } else{
            Page<Product> products = productRepository.findByIdLessThan(requestDto.getLastArticleId(), pageRequest);
            return toDtoList(products);
        }
    }

     // location 전체 page Dto 반환
    private List<PageProductResponseDto> getPageLocationResponseDtoList(PageRequest pageRequest, String location, PageRequestDto requestDto){
        if (requestDto.getLastArticleId() == null) {
            Page<Product> products = productRepository.findByLocation(location, pageRequest);
            return toDtoList(products);
        } else{
            Page<Product> products = productRepository.findByIdLessThanAndLocation(requestDto.getLastArticleId(), location, pageRequest);
            return toDtoList(products);
        }

    }


    // 상품 id로 조회
    @Transactional(readOnly = true)
    public Product getPropductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 id 입니다"));
    }

    // 상품 작성자만이 수정 ,삭제 가능
    @Transactional(readOnly = true)
    public void productMemberValidate(Member member, Product product) {
        if (!product.validateMember(member)) {
            throw new IllegalArgumentException("상품 게시글 작성자가 아닙니다");
        }
    }

    // 상품 이미지 dto에 가져오기
    @Transactional
    public void getImgResponseDto(Product product, ProductDto responseDto) {
        imgProductRepository.findAllByProduct(product).forEach(imgProduct -> {
            responseDto.getImgProductList().add(new ImgResponseDto(imgProduct.getImgUrl()));
        });
    }

    // 전체 상품 가져오기
    @Transactional
    public List<ProductAllResponseDto> getProductAllResponseDto(List<Product> productList) {
        List<ProductAllResponseDto> products = new ArrayList<>();
        productList.forEach(product -> {
            ProductAllResponseDto responseDto = new ProductAllResponseDto(product);
            getImgResponseDto(product, responseDto);
            products.add(responseDto);
        });
        return products;
    }

    // 이미지 저장
    @Transactional
    public void imgSave(ProductRequestDto requestDto, Product product) {
        requestDto.getImgProductList().forEach(imgProduct -> {
            imgProduct.setProduct(product);
            imgProductRepository.save(imgProduct);
        });
    }


    // Page를 List로 변환
    @Transactional
    public List<PageProductResponseDto> toDtoList(Page<Product> products) {

        List<Product> productList = products.getContent();
        List<PageProductResponseDto> pageProductResponseDtoList = new ArrayList<>();
        for (Product product : productList) {
            PageProductResponseDto pageProductResponseDto = new PageProductResponseDto(product);
            List<ImgProduct> imgProductList = imgProductRepository.findAllByProduct(product);
            for (ImgProduct imgProduct : imgProductList) {
                pageProductResponseDto.getImgProductList().add(new ImgResponseDto(imgProduct.getImgUrl()));
            }
            pageProductResponseDtoList.add(pageProductResponseDto);
        }
        return pageProductResponseDtoList;

    }

}
