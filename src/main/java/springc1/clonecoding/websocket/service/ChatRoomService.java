package springc1.clonecoding.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.domain.Member;
import springc1.clonecoding.domain.Product;
import springc1.clonecoding.repository.MemberRepository;
import springc1.clonecoding.repository.ProductRepository;
import springc1.clonecoding.websocket.domain.ChatMessage;
import springc1.clonecoding.websocket.domain.ChatRoom;
import springc1.clonecoding.websocket.domain.ChatRoomMember;
import springc1.clonecoding.websocket.dto.response.ChatMessageResponseDto;
import springc1.clonecoding.websocket.dto.request.ChatRoomDto;
import springc1.clonecoding.websocket.dto.response.ChatRoomResponseDto;
import springc1.clonecoding.websocket.repository.ChatMessageRepository;
import springc1.clonecoding.websocket.repository.ChatRoomMemberRepository;
import springc1.clonecoding.websocket.repository.ChatRoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ResponseDto<?> existRoomCheck(String nickname, Long productId) {

        ChatRoom chatRoom = findExistChatRoom(nickname,productId);

        if( chatRoom != null){
            return ResponseDto.success("exist");
        } else{
            throw new IllegalArgumentException("방이 존재하지 않습니다");
        }
    }


    public ResponseDto<?> getChatMessage(String nickname, Long productId) {

        ChatRoom chatRoom = findExistChatRoom(nickname, productId);

        System.out.println(chatRoom.getRoomId());
        String roomId = chatRoom.getRoomId();

        return ResponseDto.success(getChatMessageResponseDtoList(roomId));
    }


    public ResponseDto<ChatRoomResponseDto> createChatRoom(ChatRoomDto dto) {

        String senderNick = dto.getNickname();
        Member sender = memberRepository.findByNickname(senderNick).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

        Long productId = dto.getProductId();
        Product findProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 id 입니다"));
        Member receiver = findProduct.getMember();

        if(findExistChatRoom(senderNick,productId)!=null) {
            throw new IllegalArgumentException("방이 존재합니다");
        };

        if (sender.equals(receiver)) {
            throw new IllegalArgumentException("내 게시물입니다");
        } else {
            ChatRoom chatRoom = new ChatRoom(findProduct);
            chatRoomRepository.save(chatRoom);

            ChatRoomMember chatRoomMember1 = new ChatRoomMember(sender , chatRoom);
            ChatRoomMember chatRoomMember2 = new ChatRoomMember(receiver, chatRoom);
            chatRoomMemberRepository.save(chatRoomMember1);
            chatRoomMemberRepository.save(chatRoomMember2);

            ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(chatRoom.getRoomId(),chatRoom.getProduct().getName());

            return ResponseDto.success(chatRoomResponseDto);
        }


    }

    public ResponseDto<?> getChatRoom(String nickname) {
        Member findMember = memberRepository.findByNickname(nickname).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));
        List<ChatRoomMember> chatRoomMemberList = chatRoomMemberRepository.findAllByMember(findMember);

        List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
        chatRoomMemberList.forEach(chatRoomMember -> {
            ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(chatRoomMember.getChatRoom());
            chatRoomResponseDtoList.add(chatRoomResponseDto);
        });
        return ResponseDto.success(chatRoomResponseDtoList);
    }





    // 전체 page Dto 반환
    private List<ChatMessageResponseDto> getChatMessageResponseDtoList(String roomId){

        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId);

            return chatMessages.stream().map(ChatMessageResponseDto::new).collect(Collectors.toList());
    }



    public ChatRoom findExistChatRoom(String nickname, Long productId) {

        Member sender = memberRepository.findByNickname(nickname).orElseThrow(() -> new IllegalArgumentException("채팅방 존재하지 않습니다"));

        Product findProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("채팅방 존재하지 않습니다"));
        Member receiver = findProduct.getMember();

        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByProduct(findProduct);
        for (ChatRoom chatRoom : chatRoomList) {
            if (chatRoom.getChatRoomMembers().contains(chatRoomMemberRepository.findByMemberAndChatRoom(sender,chatRoom).orElse(new ChatRoomMember()))&&
                    chatRoom.getChatRoomMembers().contains(chatRoomMemberRepository.findByMemberAndChatRoom(receiver,chatRoom).orElse(new ChatRoomMember()))){
                return chatRoom;
            }
        } return null;

    }
}
