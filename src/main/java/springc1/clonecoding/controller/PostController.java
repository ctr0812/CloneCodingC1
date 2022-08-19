package springc1.clonecoding.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springc1.clonecoding.controller.request.PostRequestDto;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.domain.UserDetailsImpl;
import springc1.clonecoding.service.PostService;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;


    //api 게시글 작성
    @PostMapping("/api/post")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails);
    }

    //api 게시글 수정
    @PutMapping("/api/post/{id}")
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, postRequestDto, userDetails);
    }


    //api 게시글 삭제
    @DeleteMapping( "/api/post/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id,userDetails);
    }

    //api 특정 지역 게시글 전체 조회
    @GetMapping( "/api/post/{location}")
    public ResponseDto<?> getLocationPost(@PathVariable String location) {
        return postService.getLocationPost(location);
    }


    //api 전체 지역 게시글 전체 조회
    @GetMapping( "/api/post")
    public ResponseDto<?> getAllPost() {
        return postService.getAllPost();
    }


    //api id로 게시글 상세 조회
    @GetMapping( "/api/post/id/{id}")
    public ResponseDto<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }


}