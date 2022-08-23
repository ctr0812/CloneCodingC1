package springc1.clonecoding.websocket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import springc1.clonecoding.websocket.domain.ChatMessage;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findByIdLessThan(Long lastArticleId, PageRequest pageRequest);
}
