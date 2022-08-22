package springc1.clonecoding.websocket.domain;

import lombok.Getter;
import lombok.Setter;
import springc1.clonecoding.domain.Member;

import javax.persistence.*;

@Getter
@Setter
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
    private Member chatRoom;
}
