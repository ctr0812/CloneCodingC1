package springc1.clonecoding.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDto {
    private Long lastArticleId;
    private int size;
}
