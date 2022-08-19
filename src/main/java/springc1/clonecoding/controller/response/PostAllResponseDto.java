package springc1.clonecoding.controller.response;

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
public class PostAllResponseDto {

    private Long id;
    private String title;
    private String location;
    private String nickname;
    private List<ImgResponseDto> imgPostList = new ArrayList<>();

    public PostAllResponseDto(Post post){
        this.id = post.getId();
        this.nickname = post.getMember().getNickname();
        this.title = post.getTitle();
        this.location = post.getMember().getLocation();
    }
}
