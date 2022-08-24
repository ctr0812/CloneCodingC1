package springc1.clonecoding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.clonecoding.controller.request.*;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.domain.Member;
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
    public ResponseEntity<String> signup(SignupRequestDto requestDto) {


        String userNamePattern = "^[A-Za-z[0-9]]{4,12}$";  // 영어, 숫자 4자이상 12자 이하
        String nicknamePattern = "^[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣~!@#$%^&*]{2,8}";  // 영어 , 한글 , 특수문자 , 2자이상 8자이하
        String passwordPattern =  "(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{4,12}";  // 영어, 숫자, 특수문자 4자이상 12자 이하

        String username = requestDto.getUsername();
        String nickname = requestDto.getNickname();
        String password = requestDto.getPassword();

        if(username.equals(""))
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        else if(!Pattern.matches(userNamePattern,username))
            throw new IllegalArgumentException("아이디 형식이 맞지 않습니다.");
        else if (memberRepository.findByUsername(username).isPresent())
            throw new IllegalArgumentException("중복된 아이디가 존재합니다.");

        if(nickname.equals(""))
            throw new IllegalArgumentException("닉네임을 입력해주세요.");
        else if(!Pattern.matches(nicknamePattern,nickname))
            throw new IllegalArgumentException("닉네임 형식이 맞지 않습니다.");
        else if (memberRepository.findByNickname(nickname).isPresent())
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");

        if(password.equals(""))
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        else if ( 4 > password.length() || 12 < password.length() )
            throw new IllegalArgumentException("비밀번호 길이를 확인해주세요.");
        else if (!Pattern.matches(passwordPattern, password))
            throw new IllegalArgumentException("비밀번호 형식이 맞지 않습니다.");


        password = passwordEncoder.encode(password);

        Member member = new Member(requestDto, password);

        memberRepository.save(member);
        return new ResponseEntity<>("회원가입 축하합니다!", HttpStatus.OK);

    }


    @Transactional
    public ResponseEntity<String> userCheck(UserCheckRequestDto requestDto) {

        String userNamePattern = "^[A-Za-z[0-9]]{4,12}$";  // 영어, 숫자 4자이상 12자 이하
        String username = requestDto.getUsername();

        if(username.equals(""))
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        else if(!Pattern.matches(userNamePattern, username))
            throw new IllegalArgumentException("아이디 형식이 맞지 않습니다.");
        else if (memberRepository.findByUsername(username).isPresent())
            throw new IllegalArgumentException("중복된 아이디가 존재합니다.");

        return new ResponseEntity<>("사용 가능한 아이디입니다", HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<String> nickCheck(NickCheckRequestDto requestDto) {

        String nicknamePattern = "^[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣~!@#$%^&*]{2,8}";  // 영어 , 한글 , 특수문자 , 2자이상 8자이하
        String nickname = requestDto.getNickname();

        if(nickname.equals(""))
            throw new IllegalArgumentException("닉네임을 입력해주세요.");
        else if(!Pattern.matches(nicknamePattern, nickname))
            throw new IllegalArgumentException("닉네임 형식이 맞지 않습니다.");
        else if (memberRepository.findByNickname(nickname).isPresent())
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");


        return new ResponseEntity<>("사용 가능한 닉네임입니다", HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<String> login(LoginRequestDto requestDto, HttpServletResponse response) {

        Member member = memberRepository.findByUsername(requestDto.getUsername()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        passwordCheck(member.getPassword(), requestDto.getPassword());

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);

        tokenToHeaders(tokenDto, response);

        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);

    }



    private void passwordCheck(String password, String comfirmPassword) {
        if (passwordEncoder.matches(password, comfirmPassword)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Access_Token", "Bearer " + tokenDto.getAccessToken());
    }


}
