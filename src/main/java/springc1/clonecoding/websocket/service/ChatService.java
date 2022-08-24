package springc1.clonecoding.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import springc1.clonecoding.websocket.domain.ChatMessage;
import springc1.clonecoding.websocket.dto.request.ChatMessageDto;
import springc1.clonecoding.websocket.repository.ChatMessageRepository;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;

    public void sendMessage(ChatMessageDto message) {

        ChatMessage chatMessage = new ChatMessage(message);
        chatMessageRepository.save(chatMessage);

        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}
