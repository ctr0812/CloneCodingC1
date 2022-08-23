package springc1.clonecoding.websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.websocket.domain.ChatRoom;
import springc1.clonecoding.websocket.dto.ChatRoomCheckDto;
import springc1.clonecoding.websocket.service.ChatRoomService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;


    @GetMapping("/check")
    public ResponseDto<?> check(@RequestBody ChatRoomCheckDto dto) {
        return chatRoomService.check(dto);
    }


    // 채팅 메시지 조회
    @GetMapping("/chat/chatMessage")
    public ResponseDto<?> getChatMessage(@RequestBody ChatRoomCheckDto dto) {
        return chatRoomService.getChatMessage(dto);
    }


    // 채팅방 생성
    @PostMapping("/chat/chatRoom")
    public ResponseDto<?> createChatRoom(@RequestBody ChatRoomCheckDto dto) {
        return chatRoomService.createChatRoom(dto);
    }
}