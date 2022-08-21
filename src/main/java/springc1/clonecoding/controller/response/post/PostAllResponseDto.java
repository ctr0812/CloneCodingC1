package springc1.clonecoding.controller.response.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import springc1.clonecoding.domain.Post;


@Getter
@NoArgsConstructor
public class PostAllResponseDto extends PostDto{

    public PostAllResponseDto(Post post){
        this.id = post.getId();
        this.nickname = post.getMember().getNickname();
        this.title = post.getTitle();
        this.location = post.getMember().getLocation();
    }
}
