package springc1.clonecoding.websocket.domain;

import lombok.*;
import springc1.clonecoding.domain.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String roomId;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    public ChatRoom (Product product) {
        this.product = product;
        this.roomId = UUID.randomUUID().toString();
    }
}