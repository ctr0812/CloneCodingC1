package springc1.clonecoding.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springc1.clonecoding.websocket.domain.ChatMessage;
import springc1.clonecoding.websocket.domain.ChatRoom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class ChatRoomService {


    public void sendChatMessage(ChatMessage chatMessage) {
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String dTime = formatter.format(systemTime);
        chatMessage.setTimenow(dTime);
    }



    public List<ChatRoom> getAllRoom() {
        return new ArrayList<>();
    }


    public ChatRoom createChatRoom() {
        ChatRoom chatRoom = ChatRoom.create();
        return chatRoom;
    }

    public ChatRoom getRoom(String rommId) {
        return new ChatRoom();
    }

}
