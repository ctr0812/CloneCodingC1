package springc1.clonecoding.websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import springc1.clonecoding.websocket.dto.request.ChatMessageDto;
import springc1.clonecoding.websocket.service.ChatService;


@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message) {
        chatService.sendMessage(message);
    }
}