package springc1.clonecoding.controller.response.post;


import lombok.Getter;
import lombok.Setter;
import springc1.clonecoding.controller.response.ImgResponseDto;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PostDto {
    protected Long id;
    protected String title;
    protected String location;
    protected String nickname;
    protected List<ImgResponseDto> imgPostList = new ArrayList<>();

}
