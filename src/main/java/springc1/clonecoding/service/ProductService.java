package springc1.clonecoding.service;

import aj.org.objectweb.asm.ConstantDynamic;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.clonecoding.controller.request.PageRequestDto;
import springc1.clonecoding.controller.request.ProductRequestDto;
import springc1.clonecoding.controller.response.*;
import springc1.clonecoding.domain.*;
import springc1.clonecoding.repository.ImgProductRepository;
import springc1.clonecoding.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ImgProductRepository imgProductRepository;

    @Transactional
    public ResponseDto<?> createProduct(ProductRequestDto requestDto , UserDetailsImpl userDetails) {
         // 로그인된 멤버 정보 가져오기
        Member member = userDetails.getMember();
         // 새로운 상품 생성
        Product product = new Product(requestDto, member);
         // db에 product 저장
        productRepository.save(product);
        // 이미지 저장
        imgSave(requestDto, product);
        return ResponseDto.success("success");
    }

    @Transactional
    public ResponseDto<?> updateProduct(Long id, ProductRequestDto requestDto, UserDetailsImpl userDetails) {
        // 로그인된 멤버 정보 가져오기
        Member member = userDetails.getMember();
        // id 로 상품게시글 존재 유무 확인
        Product product = isPresentProduct(id);
        // 상품게시글 작성자만이 수정 ,삭제 가능
        memberValidateProduct(member, product);
        // product 업데이트
        product.update(requestDto);
        // 이미지 저장
        imgSave(requestDto, product);
        return ResponseDto.success("success");
    }

    @Transactional
    public ResponseDto<?> deleteProduct(Long id, UserDetailsImpl userDetails) {
        // 로그인된 멤버 정보 가져오기
        Member member = userDetails.getMember();
        // id 로 상품게시글 존재 유무 확인
        Product product = isPresentProduct(id);
        // 상품게시글 작성자만이 수정 ,삭제 가능
        memberValidateProduct(member, product);
        productRepository.delete(product);
        return ResponseDto.success("success");

    }

    @Transactional
    public ResponseDto<?> getLocationProduct(String location) {
        // db에서 location 값으로 상품 전체 가져오기
        List<Product> productList = productRepository.findAllByLocationOrderByIdDesc(location);
        // 전체 상품 dto 반환
        return ResponseDto.success(getAllProductResponseDto(productList));
    }



    @Transactional
    public ResponseDto<?> getAllProduct() {
        // db에서 상품 전체 가져오기
        List<Product> productList = productRepository.findAllByOrderByCreatedAtDesc();
        // 전체 상품 dto 반환
        return ResponseDto.success(getAllProductResponseDto(productList));
    }


    @Transactional
    public ResponseDto<?> getProduct(Long id) {

        // id 로 상품게시글 존재 유무 확인
        Product product = isPresentProduct(id);
        ProductResponseDto responseDto = new ProductResponseDto(product);
        //상품 이미지 가져오기
        List<ImgProduct> imgProductList = imgProductRepository.findAllByProduct(product);
        for (ImgProduct imgProduct : imgProductList) {
            responseDto.getImgProductList().add(new ImgResponseDto(imgProduct.getImgUrl()));
        }
        // 상품 상세 조회 dto 반환
        return ResponseDto.success(responseDto);


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


    // 상품 이미지 dto에 가져오기
    @Transactional
    public void getImgResponse(Product product, ProductAllResponseDto responseDto) {
        List<ImgProduct> imgProductList = imgProductRepository.findAllByProduct(product);
        for (ImgProduct imgProduct : imgProductList) {
            responseDto.getImgProductList().add(new ImgResponseDto(imgProduct.getImgUrl()));
        }

    }

    // 전체 상품 가져오기
    @Transactional
    public List<ProductAllResponseDto> getAllProductResponseDto(List<Product> productList) {
        List<ProductAllResponseDto> products = new ArrayList<>();

        for (Product product : productList) {
            ProductAllResponseDto responseDto = new ProductAllResponseDto(product);
            //상품 이미지 가져오기
            getImgResponse(product, responseDto);
            products.add(responseDto);
        }
        return products;
    }

    // 이미지 저장
    @Transactional
    public void imgSave(ProductRequestDto requestDto, Product product) {
        List<ImgProduct> imgProductList = requestDto.getImgProductList();
        for (ImgProduct imgProduct : imgProductList) {
            imgProduct.setProduct(product);
            imgProductRepository.save(imgProduct);
        }
    }

    @Transactional
    public ResponseDto<?> getAllProductPagesBy(PageRequestDto reqeustDto) {

        PageRequest pageRequest = PageRequest.of(0, reqeustDto.getSize(), Sort.by("id").descending());
        Page<Product> products = productRepository.findByIdLessThan(reqeustDto.getLastArticleId(), pageRequest);
        List<PageResponseDto> pageResponseDtoList = toDtoList(products);

        return ResponseDto.success(pageResponseDtoList);
    }


    @Transactional
    public ResponseDto<?> getAllProductLocationPagesBy(PageRequestDto requestDto, String location) {
        PageRequest pageRequest = PageRequest.of(0, requestDto.getSize(),Sort.by("id").descending());
        Page<Product> products = productRepository.findByIdLessThanAndLocation(requestDto.getLastArticleId(), location, pageRequest);
        List<PageResponseDto> pageResponseDtoList = toDtoList(products);

        return ResponseDto.success(pageResponseDtoList);
    }


    // Page를 List로 변환
    @Transactional
    public List<PageResponseDto> toDtoList(Page<Product> products) {

        List<Product> productList = products.getContent();
        List<PageResponseDto> pageResponseDtoList = new ArrayList<>();
        for (Product product : productList) {
            PageResponseDto pageResponseDto = new PageResponseDto(product);
            List<ImgProduct> imgProductList = imgProductRepository.findAllByProduct(product);
            for (ImgProduct imgProduct : imgProductList) {
                pageResponseDto.getImgProductList().add(new ImgResponseDto(imgProduct.getImgUrl()));
            }
            pageResponseDtoList.add(pageResponseDto);
        }
        return pageResponseDtoList;

    }

}
