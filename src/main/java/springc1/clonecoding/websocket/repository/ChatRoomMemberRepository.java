package springc1.clonecoding.websocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springc1.clonecoding.domain.Member;
import springc1.clonecoding.websocket.domain.ChatRoom;
import springc1.clonecoding.websocket.domain.ChatRoomMember;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository  extends JpaRepository<ChatRoomMember, Long> {
    Optional<ChatRoomMember> findByMemberAndChatRoom(Member member, ChatRoom chatRoom);

    List<ChatRoomMember> findAllByMember(Member findMember);
}
