package springc1.clonecoding.controller.response.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springc1.clonecoding.controller.response.CommentResponseDto;
import springc1.clonecoding.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto extends PostDto{

    private String content;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> comments;

    public PostResponseDto(Post post ,List<CommentResponseDto> comments){
        this.id = post.getId();
        this.nickname = post.getMember().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.location = post.getMember().getLocation();
        this.createdAt = post.getCreatedAt();
        this.comments = comments;
    }
}
