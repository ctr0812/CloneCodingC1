package springc1.clonecoding.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.clonecoding.websocket.domain.ChatMessage;
import springc1.clonecoding.websocket.domain.ChatRoom;
import springc1.clonecoding.websocket.dto.request.ChatMessageDto;
import springc1.clonecoding.websocket.repository.ChatMessageRepository;
import springc1.clonecoding.websocket.repository.ChatRoomRepository;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void sendMessage(ChatMessageDto message) {

        ChatMessage chatMessage = new ChatMessage(message);
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(message.getRoomId()).orElseThrow(()-> new IllegalArgumentException("잘못된 채팅방입니다."));
        chatMessage.setChatRoom(chatRoom);

        chatMessageRepository.save(chatMessage);

        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}
