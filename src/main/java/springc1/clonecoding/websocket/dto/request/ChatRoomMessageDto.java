package springc1.clonecoding.websocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomMessageDto {

    private String nickname;
    private Long productId;
    private Long lastArticleId;
    private int size;

}
