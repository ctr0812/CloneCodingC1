package springc1.clonecoding.websocket.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springc1.clonecoding.websocket.domain.ChatRoom;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomResponseDto {

    private String roomId;
    private String productName;

    public ChatRoomResponseDto(String roomId, String productName) {
        this.roomId = roomId;
        this.productName = productName;
    }

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.productName = chatRoom.getProduct().getName();
    }
}
