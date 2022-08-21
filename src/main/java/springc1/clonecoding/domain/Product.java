package springc1.clonecoding.domain;

import lombok.*;
import springc1.clonecoding.controller.request.ProductRequestDto;

import javax.persistence.*;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String location;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ImgProduct> imgProductList;

    public Product(ProductRequestDto requestDto, Member member) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.content = requestDto.getContent();
        this.member = member;
        this.location = member.getLocation();
    }

    public void update(ProductRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.content = requestDto.getContent();
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }
}
