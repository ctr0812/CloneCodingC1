package springc1.clonecoding.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springc1.clonecoding.controller.request.LoginRequestDto;
import springc1.clonecoding.controller.request.NickCheckRequestDto;
import springc1.clonecoding.controller.request.SignupRequestDto;
import springc1.clonecoding.controller.request.UserCheckRequestDto;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.service.MemberService;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    //api 회원가입
    @PostMapping(value = "/user/signup")
    public ResponseDto<?> signup(@RequestBody SignupRequestDto requestDto) {
        return memberService.createMember(requestDto);
    }

    //api 아이디 중복체크
    @PostMapping(value = "/user/signup/usercheck")
    public ResponseDto<?> usercheck(@RequestBody UserCheckRequestDto requestDto){
        return memberService.userCheck(requestDto);
    }

    //api 닉네임 중복체크
    @PostMapping(value = "/user/signup/nickcheck")
    public ResponseDto<?> usercheck(@RequestBody NickCheckRequestDto requestDto){
        return memberService.nickCheck(requestDto);
    }

    //api 로그인
    @PostMapping(value = "/user/login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto requestDto,
                                HttpServletResponse response
    ) {
        return memberService.login(requestDto, response);
    }
}