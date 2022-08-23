package springc1.clonecoding.websocket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomResponseDto {

    private String roomId;

    public ChatRoomResponseDto(String roomId) {
        this.roomId = roomId;
    }
}
