package springc1.clonecoding.websocket.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springc1.clonecoding.websocket.domain.ChatMessage;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {

    private String nickname;
    private String roomId;
    private String message;

    public ChatMessageResponseDto(ChatMessage chatMessage) {
        this.nickname = chatMessage.getNickname();
        this.roomId = chatMessage.getRoomId();
        this.message = chatMessage.getMessage();

    }
}
