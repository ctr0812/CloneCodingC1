package springc1.clonecoding.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomCheckDto {

    private String nickname;
    private Long productId;
    private Long lastArticleId;
    private int size;
}
