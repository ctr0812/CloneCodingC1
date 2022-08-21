package springc1.clonecoding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.clonecoding.controller.request.*;
import springc1.clonecoding.controller.response.MemberResponseDto;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.domain.Member;
import springc1.clonecoding.jwt.TokenProvider;
import springc1.clonecoding.repository.MemberRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createMember(SignupRequestDto requestDto) {

        //패스워드 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());
        // 멤버 생성
        Member member = new Member(requestDto, password);
        //db에 멤버 저장
        memberRepository.save(member);
        return ResponseDto.success("success");

    }


    @Transactional
    public ResponseDto<?> userCheck(UserCheckRequestDto requestDto) {
        memberRepository.findByUsername(requestDto.getUsername()).orElseThrow(() -> new IllegalArgumentException("중복된 아이디가 존재합니다."));
        return ResponseDto.success("success");
    }


    @Transactional
    public ResponseDto<?> nickCheck(NickCheckRequestDto requestDto) {
        memberRepository.findByUsername(requestDto.getNickname()).orElseThrow(() -> new IllegalArgumentException("중복된 아이디가 존재합니다."));
        return ResponseDto.success("success");

    }


    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {

        // 아이디 존재 확인
        Member member = memberRepository.findByUsername(requestDto.getUsername()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 비밀번호 확인
        passwordCheck(member.getPassword(), requestDto.getPassword());
        // 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        // response header에 토큰 저장
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success("success");

    }
    private void passwordCheck(String password, String comfirmPassword) {
        if (passwordEncoder.matches(password, comfirmPassword)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    // response header에 토큰 저장
    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Access-Token", "Bearer " + tokenDto.getAccessToken());
    }


}
