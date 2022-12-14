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

        Post post = postService.getPostById(requestDto.getPostId());

        Comment comment = Comment.of(member, post, requestDto.getContent());
        commentRepository.save(comment);
        return ResponseDto.success("success");
    }


    @Transactional
    public ResponseDto<?> updateComment(Long id, CommentRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Comment comment = getCommentById(id);

        memberValidateComment(member, comment);

        comment.update(requestDto.getContent());
        return ResponseDto.success("success");
    }


    @Transactional
    public ResponseDto<?> deleteComment(Long id, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Comment comment = getCommentById(id);

        memberValidateComment(member, comment);

        commentRepository.delete(comment);
        return ResponseDto.success("success");

    }

    private void memberValidateComment(Member member, Comment comment) {
        if (!comment.getMember().equals(member)) {
            throw new IllegalArgumentException("?????? ???????????? ????????????");
        }
    }

    @Transactional(readOnly = true)
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ?????? id ?????????"));
    }


    }
