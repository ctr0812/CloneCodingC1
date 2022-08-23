package springc1.clonecoding.websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import springc1.clonecoding.websocket.domain.ChatMessage;
import springc1.clonecoding.websocket.domain.ChatMessageDto;


@RequiredArgsConstructor
    @Controller
    public class ChatController {

        private final SimpMessageSendingOperations messagingTemplate;

        @MessageMapping("/chat/message")
        public void message(ChatMessageDto message) {
            System.out.println("pub/chat/실행되었습니다");

                message.setMessage(message.getMessage()+ "제가 추가했습니다");
                messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

        }
    }