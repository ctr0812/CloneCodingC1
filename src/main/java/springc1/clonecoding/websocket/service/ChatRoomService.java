package springc1.clonecoding.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.domain.Member;
import springc1.clonecoding.domain.Product;
import springc1.clonecoding.repository.MemberRepository;
import springc1.clonecoding.repository.ProductRepository;
import springc1.clonecoding.websocket.domain.ChatMessage;
import springc1.clonecoding.websocket.domain.ChatRoom;
import springc1.clonecoding.websocket.domain.ChatRoomMember;
import springc1.clonecoding.websocket.dto.ChatMessageResponseDto;
import springc1.clonecoding.websocket.dto.ChatRoomCheckDto;
import springc1.clonecoding.websocket.dto.ChatRoomResponseDto;
import springc1.clonecoding.websocket.repository.ChatMessageRepository;
import springc1.clonecoding.websocket.repository.ChatRoomMemberRepository;
import springc1.clonecoding.websocket.repository.ChatRoomRepository;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ResponseDto<?> check(ChatRoomCheckDto dto) {
        String senderNick = dto.getNickname();
        Member sender = memberRepository.findByNickname(senderNick).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

        Long productId = dto.getProductId();
        Product findProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 id 입니다"));
        Member receiver = findProduct.getMember();

        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByProduct(findProduct);

        if( existChatRoom(chatRoomList, sender, receiver) != null){
            return ResponseDto.success("exist");
        } else{
            return ResponseDto.success("create");
        }

    }


    public ResponseDto<?> getChatMessage(ChatRoomCheckDto dto) {

        PageRequest pageRequest = PageRequest.of(0, dto.getSize(), Sort.by("id").descending());
        return ResponseDto.success(getChatMessageResponseDtoList(pageRequest,dto));

    }


    public ResponseDto<ChatRoomResponseDto> createChatRoom(ChatRoomCheckDto dto) {

        String senderNick = dto.getNickname();
        Member sender = memberRepository.findByNickname(senderNick).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

        Long productId = dto.getProductId();
        Product findProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 id 입니다"));
        Member receiver = findProduct.getMember();

        ChatRoom chatRoom = new ChatRoom(findProduct);
        chatRoomRepository.save(chatRoom);

        ChatRoomMember chatRoomMember1 = new ChatRoomMember(sender , chatRoom);
        ChatRoomMember chatRoomMember2 = new ChatRoomMember(receiver, chatRoom);
        chatRoomMemberRepository.save(chatRoomMember1);
        chatRoomMemberRepository.save(chatRoomMember2);

        ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(chatRoom.getRoomId());

        return ResponseDto.success(chatRoomResponseDto);
    }



    // 전체 page Dto 반환
    private List<ChatMessageResponseDto> getChatMessageResponseDtoList(PageRequest pageRequest, ChatRoomCheckDto dto) {
        if (dto.getLastArticleId() == null) {
            Page<ChatMessage> chatMessages = chatMessageRepository.findAll(pageRequest);
            return toDtoList(chatMessages);
        } else{
            Page<ChatMessage> chatMessages =  chatMessageRepository.findByIdLessThan(dto.getLastArticleId(), pageRequest);
            return toDtoList(chatMessages);
        }
    }


    // Page를 List로 변환
    @Transactional
    public List<ChatMessageResponseDto> toDtoList(Page<ChatMessage> chatMessages) {
        return chatMessages.map(ChatMessageResponseDto::new).toList();
    }

    public ChatRoom existChatRoom(List<ChatRoom> chatRoomList , Member sender, Member receiver) {
        for (ChatRoom chatRoom : chatRoomList) {
            if (chatRoom.getChatRoomMembers().contains(chatRoomMemberRepository.findByMember(sender)))
                if (chatRoom.getChatRoomMembers().contains(chatRoomMemberRepository.findByMember(receiver))) {
                    return chatRoom;
                }
        } return null;
    }


}
