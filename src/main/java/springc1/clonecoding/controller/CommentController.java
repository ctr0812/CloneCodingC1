package springc1.clonecoding.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springc1.clonecoding.controller.request.CommentRequestDto;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.domain.UserDetailsImpl;
import springc1.clonecoding.service.CommentService;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    //api 댓글 작성
    @PostMapping(value = "/api/comment")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(requestDto, userDetails);
    }

    //api 댓글 수정
    @PutMapping(value = "/api/comment/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, requestDto, userDetails);
    }

    //api 댓글 삭제
    @DeleteMapping(value = "/api/comment/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails);
    }
}