package springc1.clonecoding.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private String nickname;

    public MemberResponseDto(String nickname) {
        this.nickname = nickname;
    }
}
