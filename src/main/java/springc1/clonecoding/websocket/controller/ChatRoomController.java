package springc1.clonecoding.websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.websocket.dto.request.ChatRoomDto;
import springc1.clonecoding.websocket.service.ChatRoomService;


@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;


    // 채팅방 유무 확인
    @GetMapping("/chat/check/{nickname}/{productId}")
    public ResponseDto<?> existRoomCheck(@PathVariable String nickname, @PathVariable Long productId) {
        return chatRoomService.existRoomCheck(nickname,productId);
    }
    

    // 채팅 메시지 조회
    @GetMapping("/chat/chatMessage/{nickname}/{productId}")
    public ResponseDto<?> getChatMessage(@PathVariable String nickname, @PathVariable Long productId) {
        return chatRoomService.getChatMessage(nickname,productId);
    }


    // 채팅방 생성
    @PostMapping("/chat/chatRoom")
    public ResponseDto<?> createChatRoom(@RequestBody ChatRoomDto dto) {
        return chatRoomService.createChatRoom(dto);
    }
}