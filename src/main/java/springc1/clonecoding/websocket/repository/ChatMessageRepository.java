package springc1.clonecoding.websocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springc1.clonecoding.websocket.domain.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoomIdOrderByTimenowDesc(String RoomId);
}
