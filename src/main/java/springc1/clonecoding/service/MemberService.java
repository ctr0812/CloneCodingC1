package springc1.clonecoding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.clonecoding.controller.request.*;
import springc1.clonecoding.controller.response.MemberResponseDto;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.domain.Member;
import springc1.clonecoding.exception.CustomException;
import springc1.clonecoding.exception.ErrorCode;
import springc1.clonecoding.jwt.TokenProvider;
import springc1.clonecoding.repository.MemberRepository;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<String> signup(SignupRequestDto requestDto) {

        validCheck(requestDto);

        String password = passwordEncoder.encode(requestDto.getPassword());

        Member member = new Member(requestDto, password);

        memberRepository.save(member);
        return ResponseDto.success("회원가입 축하합니다!");

    }




    @Transactional
    public ResponseDto<String> userCheck(String username) {

        String userNamePattern = "^[A-Za-z[0-9]]{4,12}$";  // 영어, 숫자 4자이상 12자 이하

        if (username.equals(""))
            return ResponseDto.fail("Bad_Request","아이디를 입력해주세요.");
        else if (memberRepository.findByUsername(username).isPresent())
            return ResponseDto.fail("Bad_Request","중복된 아이디가 존재합니다");
        else if (!Pattern.matches(userNamePattern, username))
            return ResponseDto.fail("Bad_Request","아이디는 영문, 숫자 모두 가능합니다");

        return ResponseDto.success("사용 가능한 아이디입니다");
    }


    @Transactional
    public ResponseDto<String> nickCheck(String nickname) {

        String nicknamePattern = "^[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣~!@#$%^&*]{2,8}";  // 영어 , 한글 , 특수문자 , 2자이상 8자이하

        if (nickname.equals(""))
            return ResponseDto.fail("Bad_Request","닉네임을 입력해주세요.");
        else if (memberRepository.findByNickname(nickname).isPresent())
            return ResponseDto.fail("Bad_Request","중복된 닉네임이 존재합니다");
        else if (2 > nickname.length() || 8 < nickname.length())
            return ResponseDto.fail("Bad_Request","닉네임은 2자 이상 8자 이하여야 합니다");
        else if (!Pattern.matches(nicknamePattern, nickname))
            return ResponseDto.fail("Bad_Request","닉네임은 영문, 한글, 특수문자 다 가능합니다");

        return ResponseDto.success("사용 가능한 닉네임입니다");
    }


    @Transactional
    public ResponseEntity<ResponseDto<MemberResponseDto>> login(LoginRequestDto requestDto, HttpServletResponse response) {

        Member member = memberRepository.findByUsername(requestDto.getUsername()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        passwordCheck(requestDto.getPassword(), member.getPassword());

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return new ResponseEntity<>(ResponseDto.success(new MemberResponseDto(member.getNickname())), HttpStatus.OK);

    }

    // raw password , encoded password 위치 중요
    private void passwordCheck(String password, String comfirmPassword) {
        if (!passwordEncoder.matches(password, comfirmPassword)) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Access_Token", "Bearer " + tokenDto.getAccessToken());
    }


    private void validCheck(SignupRequestDto requestDto) {
        String userNamePattern = "^[A-Za-z[0-9]]{4,12}$";  // 영어, 숫자 4자이상 12자 이하
        String nicknamePattern = "^[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣~!@#$%^&*]{2,8}";  // 영어 , 한글 , 특수문자 , 2자이상 8자이하
        String passwordPattern = "(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{4,12}";  // 영어, 숫자, 특수문자 4자이상 12자 이하

        String username = requestDto.getUsername();
        String nickname = requestDto.getNickname();
        String password = requestDto.getPassword();

        if (username.equals(""))
            throw new CustomException(ErrorCode.EMPTY_USERNAME);
        else if (memberRepository.findByUsername(username).isPresent())
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        else if (!Pattern.matches(userNamePattern, username))
            throw new CustomException(ErrorCode.USERNAME_WRONG);

        if (nickname.equals(""))
            throw new CustomException(ErrorCode.EMPTY_NICKNAME);
        else if (memberRepository.findByNickname(nickname).isPresent())
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        else if (2 > nickname.length() || 8 < nickname.length())
            throw new CustomException(ErrorCode.NICKNAME_LEGNTH);
        else if (!Pattern.matches(nicknamePattern, nickname))
            throw new CustomException(ErrorCode.NICKNAME_WRONG);

        if (password.equals(""))
            throw new CustomException(ErrorCode.EMPTY_PASSWORD);
        else if (4 > password.length() || 12 < password.length())
            throw new CustomException(ErrorCode.PASSWORD_LEGNTH);
        else if (!Pattern.matches(passwordPattern, password))
            throw new CustomException(ErrorCode.PASSWORD_WRONG);
    }
}
