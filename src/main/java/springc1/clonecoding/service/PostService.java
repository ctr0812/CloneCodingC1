package springc1.clonecoding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.clonecoding.controller.request.PostRequestDto;
import springc1.clonecoding.controller.response.*;
import springc1.clonecoding.controller.response.post.PostAllResponseDto;
import springc1.clonecoding.controller.response.post.PostDto;
import springc1.clonecoding.controller.response.post.PostResponseDto;
import springc1.clonecoding.domain.*;
import springc1.clonecoding.repository.CommentRepository;
import springc1.clonecoding.repository.ImgPostRepository;
import springc1.clonecoding.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ImgPostRepository imgPostRepository;


    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Post post = new Post(requestDto, member);

        postRepository.save(post);

        imgSave(requestDto, post);
        return ResponseDto.success("success");
    }


    @Transactional
    public ResponseDto<?> updatePost(Long id, PostRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Post post = getPostById(id);

        postMemberValidate(member, post);

        post.update(requestDto);

        imgPostRepository.deleteAll(imgPostRepository.findAllByPost(post));
        imgSave(requestDto, post);
        return ResponseDto.success("success");
    }


    @Transactional
    public ResponseDto<?> deletePost(Long id, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Post post = getPostById(id);

        postMemberValidate(member, post);

        postRepository.delete(post);
        return ResponseDto.success("success");
    }


    @Transactional(readOnly = true)
    public ResponseDto<?> getLocationPost(String location) {

        List<Post> postList = postRepository.findAllByLocationOrderByIdDesc(location);

        return ResponseDto.success(getPostAllResponseDto(postList));
    }


    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPost() {

        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();

        return ResponseDto.success(getPostAllResponseDto(postList));
    }


    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(Long id) {

        Post post = getPostById(id);

        PostResponseDto responseDto = createPostResponseDto(post);

        getImgResponseDto(post, responseDto);

        return ResponseDto.success(responseDto);
    }




      // ????????? Dto ??????
    private PostResponseDto createPostResponseDto(Post post) {
        List<CommentResponseDto> comments = getAllCommentsByPost(post);
        return new PostResponseDto(post, comments);
    }

    // ?????? ????????????
    private List<CommentResponseDto> getAllCommentsByPost(Post post) {
        return commentRepository.findAllByPost(post).stream()
                .map(CommentResponseDto::new)
                .collect(toList());
    }

    // ????????? ??????
    @Transactional
    public void imgSave(PostRequestDto requestDto, Post post) {
        requestDto.getImgPostList().forEach(imgPost -> {
            imgPost.setPost(post);
            imgPostRepository.save(imgPost);
        });
    }

    // ????????? ??????
    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ????????? id ?????????"));
    }


    // ????????? ??????????????? ?????? ,?????? ??????
    public void postMemberValidate(Member member, Post post) {
        if (!post.getMember().equals(member)) {
            throw new IllegalArgumentException("????????? ???????????? ????????????");
        }
    }

    // ?????? ????????? ????????????
    @Transactional(readOnly = true)
    public List<PostAllResponseDto> getPostAllResponseDto(List<Post> postList) {
        List<PostAllResponseDto> posts = new ArrayList<>();
        postList.forEach(post -> {
            PostAllResponseDto responseDto = new PostAllResponseDto(post);
            getImgResponseDto(post, responseDto);
            posts.add(responseDto);
        });
        return posts;
    }

    // ????????? ????????? dto??? ????????????
    @Transactional(readOnly = true)
    public void getImgResponseDto(Post post, PostDto responseDto) {
        imgPostRepository.findAllByPost(post).forEach(imgPost ->
                responseDto.getImgPostList().add(new ImgResponseDto(imgPost.getImgUrl())));
    }
}