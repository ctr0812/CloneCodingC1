package springc1.clonecoding.websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springc1.clonecoding.websocket.domain.ChatRoom;
import springc1.clonecoding.websocket.service.ChatRoomService;

import java.util.List;

// import 생략...

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;


    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    public List<ChatRoom> room() {
        return chatRoomService.getAllRoom();
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ChatRoom createRoom() {
        return chatRoomService.createChatRoom();
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomService.getRoom(roomId);
    }
}