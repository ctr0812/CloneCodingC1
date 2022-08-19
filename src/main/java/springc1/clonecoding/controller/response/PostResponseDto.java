package springc1.clonecoding.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springc1.clonecoding.domain.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String location;
    private String nickname;
    private List<ImgResponseDto> imgPostList = new ArrayList<>();
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
