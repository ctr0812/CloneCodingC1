package springc1.clonecoding.websocket.domain;

import lombok.*;
import springc1.clonecoding.domain.Member;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    public ChatRoomMember(Member member, ChatRoom chatRoom) {
        this.member = member;
        this.chatRoom = chatRoom;
    }
}
