package springc1.clonecoding.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private String nickname;
    private String location;

    public MemberResponseDto(String nickname, String location) {
        this.nickname = nickname;
        this.location = location;
    }
}
