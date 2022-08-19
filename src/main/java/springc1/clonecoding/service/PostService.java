package springc1.clonecoding.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.clonecoding.controller.request.PostRequestDto;
import springc1.clonecoding.controller.response.*;
import springc1.clonecoding.domain.*;
import springc1.clonecoding.repository.CommentRepository;
import springc1.clonecoding.repository.ImgPostRepository;
import springc1.clonecoding.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ImgPostRepository imgPostRepository;


    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, UserDetailsImpl userDetails) {
        // 로그인된 멤버 정보 가져오기
        Member member = userDetails.getMember();
        // 새로운 게시글 생성
        Post post = new Post(requestDto, member);
        // db에 post 저장
        postRepository.save(post);
        // 이미지 저장
        imgSave(requestDto, post);
        return ResponseDto.success("success");
    }

    @Transactional
    public ResponseDto<?> updatePost(Long id, PostRequestDto requestDto, UserDetailsImpl userDetails) {
        // 로그인된 멤버 정보 가져오기
        Member member = userDetails.getMember();
        // id 로 게시글 존재 유무 확인
        Post post = isPresentPost(id);
        // 게시글 작성자만이 수정 ,삭제 가능
        memberValidatePost(member, post);
        // post 업데이트
        post.update(requestDto);
        // 이미지 저장
        imgSave(requestDto, post);
        return ResponseDto.success("success");
    }

    @Transactional
    public ResponseDto<?> deletePost(Long id, UserDetailsImpl userDetails) {
        // 로그인된 멤버 정보 가져오기
        Member member = userDetails.getMember();
        // id 로 게시글 존재 유무 확인
        Post post = isPresentPost(id);
        // 게시글 작성자만이 수정 ,삭제 가능
        memberValidatePost(member, post);
        postRepository.delete(post);
        return ResponseDto.success("success");
    }

    @Transactional
    public ResponseDto<?> getLocationPost(String location) {
        // db에서 location 값으로 게시글 전체 가져오기
        List<Post> postList = postRepository.findAllByLocation(location);
        // 전체 상품 dto 반환
        return ResponseDto.success(getAllPostResponseDto(postList));
    }

    @Transactional
    public ResponseDto<?> getAllPost() {
        // db에서 상품 전체 가져오기
        List<Post> postList = postRepository.findAll();
        // 전체 상품 dto 반환
        return ResponseDto.success(getAllPostResponseDto(postList));
    }

    @Transactional
    public ResponseDto<?> getPost(Long id) {
        // id 로 상품게시글 존재 유무 확인
        Post post = isPresentPost(id);
        // 게시글에 작성된 댓글 모두 가져오기
        List<CommentResponseDto> comments = getAllCommentsByPost(post);

        PostResponseDto responseDto = new PostResponseDto(post, comments);

        //상품 이미지 가져오기
        List<ImgPost> imgPostList = imgPostRepository.findAllByPost(post);
        for (ImgPost imgPost : imgPostList) {
            responseDto.getImgPostList().add(new ImgResponseDto(imgPost.getImgUrl()));
        }
        // 상품 상세 조회 dto 반환
        return ResponseDto.success(responseDto);
    }


    // 이미지 저장
    public void imgSave(PostRequestDto requestDto, Post post) {
        List<ImgPost> imgPostList = requestDto.getImgPostList();
        for (ImgPost imgPost : imgPostList) {
            imgPost.setPost(post);
            imgPostRepository.save(imgPost);
        }
    }

    // id 로 게시글 존재 유무 확인
    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {

        Optional<Post> optionalPost = postRepository.findById(id);
        Post post = optionalPost.orElse(null);

        if (null == post) {
            throw new IllegalArgumentException("존재하지 않는 게시글 id 입니다");
        } else {
            return post;
        }
    }

    // 게시글 작성자만이 수정 ,삭제 가능
    @Transactional(readOnly = true)
    public void memberValidatePost(Member member, Post post) {
        if (post.validateMember(member)) {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다");
        }
    }

    // 게시글에 작성된 댓글 모두 가져오기
    private List<CommentResponseDto> getAllCommentsByPost(Post post) {
        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> comments = new ArrayList<>();

        for (Comment comment : commentList) {
            comments.add(new CommentResponseDto(comment));
        }
        return comments;
    }

    // 전체 게시글 가져오기
    @Transactional
    public List<PostAllResponseDto> getAllPostResponseDto(List<Post> postList) {
        List<PostAllResponseDto> posts = new ArrayList<>();

        for (Post post : postList) {
            PostAllResponseDto responseDto = new PostAllResponseDto(post);
            //상품 이미지 가져오기
            getImgResponse(post, responseDto);
            posts.add(responseDto);
        }
        return posts;
    }

    // 게시글 이미지 dto에 가져오기
    @Transactional
    public void getImgResponse(Post post, PostAllResponseDto responseDto) {
        List<ImgPost> imgPostList = imgPostRepository.findAllByPost(post);
        for (ImgPost imgPost : imgPostList) {
            responseDto.getImgPostList().add(new ImgResponseDto(imgPost.getImgUrl()));
        }
    }
}