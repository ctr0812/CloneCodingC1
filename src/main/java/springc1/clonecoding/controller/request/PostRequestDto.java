package springc1.clonecoding.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springc1.clonecoding.domain.ImgPost;
import springc1.clonecoding.domain.ImgProduct;
import springc1.clonecoding.domain.Post;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    private String title;
    private String content;
    private List<ImgPost> imgPostList;
}
