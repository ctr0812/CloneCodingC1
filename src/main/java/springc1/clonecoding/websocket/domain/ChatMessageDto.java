package springc1.clonecoding.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private String sender; // 메시지 보낸사람

    private String roomId; // 방번호

    private String message; // 메시지
}
