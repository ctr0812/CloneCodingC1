package springc1.clonecoding.websocket.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import springc1.clonecoding.domain.Product;
import springc1.clonecoding.websocket.domain.ChatRoom;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByProduct(Product findProduct);

    Optional<ChatRoom> findByRoomId(String roomId);
}