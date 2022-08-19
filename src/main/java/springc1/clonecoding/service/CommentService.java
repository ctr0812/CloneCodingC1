package springc1.clonecoding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.clonecoding.controller.request.CommentRequestDto;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.domain.Comment;
import springc1.clonecoding.domain.Member;
import springc1.clonecoding.domain.Post;
import springc1.clonecoding.domain.UserDetailsImpl;
import springc1.clonecoding.repository.CommentRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final PostService postService;

    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
        // id 로 댓글 존재 유무 확인
        Post post = postService.isPresentPost(requestDto.getPostId());
        // db 에 댓글 저장
        commentRepository.save(new Comment(member, post, requestDto));
        return ResponseDto.success("success");
    }


    @Transactional
    public ResponseDto<?> updateComment(Long id, CommentRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
        // id 로 댓글 존재 유무 확인
        postService.isPresentPost(requestDto.getPostId());
        // id 로 댓글 존재 유무 확인
        Comment comment = isPresentComment(id);
        // 댓글 작성자만이 수정 가능
        memberValidateComment(member, comment);
        // 댓글 업데이트
        comment.update(requestDto);
        return ResponseDto.success("success");
    }


    @Transactional
    public ResponseDto<?> deleteComment(Long id, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
        // id 로 댓글 존재 유무 확인
        Comment comment = isPresentComment(id);
        // 댓글 작성자만이 수정 삭제 가능
        memberValidateComment(member, comment);
        // 댓글 삭제
        commentRepository.delete(comment);
        return ResponseDto.success("success");

    }


    // id 로 댓글 존재 유무 확인
    @Transactional(readOnly = true)
    public Comment isPresentComment(Long id) {

        Optional<Comment> optionalComment = commentRepository.findById(id);
        Comment comment = optionalComment.orElse(null);

        if (null == comment) {
            throw new IllegalArgumentException("존재하지 않는 댓글 id 입니다");
        } else{
            return comment;
        }
    }

    // 댓글 작성자만이 수정 ,삭제 가능
    @Transactional(readOnly = true)
    public void memberValidateComment(Member member, Comment comment) {
        if (comment.validateMember(member)) {
            throw new IllegalArgumentException("댓글 작성자가 아닙니다");
        }
    }


    }
