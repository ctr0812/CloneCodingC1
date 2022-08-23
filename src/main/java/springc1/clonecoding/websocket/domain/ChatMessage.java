package springc1.clonecoding.websocket.domain;

import lombok.*;
import springc1.clonecoding.domain.Timestamped;
import springc1.clonecoding.websocket.dto.ChatMessageDto;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatMessage extends Timestamped {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nickname; // 메시지 보낸사람

    @Column
    private String roomId; // 방번호

    @Column
    private String message; // 메시지

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    public ChatMessage(ChatMessageDto message) {
        this.nickname = message.getNickname();
        this.roomId = message.getRoomId();
        this.message = message.getMessage();
    }

}