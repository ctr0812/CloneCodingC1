package springc1.clonecoding.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /*
    400 Bad Request
     */

    EMPTY_USERNAME(HttpStatus.BAD_REQUEST, "아이디를 입력해주세요."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "중복된 아이디가 존재합니다"),
    USERNAME_WRONG(HttpStatus.BAD_REQUEST,"아이디는 영문, 숫자 모두 가능합니다"),

    EMPTY_NICKNAME(HttpStatus.BAD_REQUEST,"닉네임을 입력해주세요."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임이 존재합니다"),
    NICKNAME_LEGNTH(HttpStatus.BAD_REQUEST, "닉네임은 2자 이상 8자 이하여야 합니다"),
    NICKNAME_WRONG(HttpStatus.BAD_REQUEST, "닉네임은 영문, 한글, 특수문자 다 가능합니다"),

    EMPTY_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호를 입력해주세요."),
    PASSWORD_WRONG(HttpStatus.BAD_REQUEST, "비밀번호는 영문, 숫자를 포함해야합니다"),
    PASSWORD_LEGNTH(HttpStatus.BAD_REQUEST, "비밀번호는 4자 이상 12자 이하여야 합니다"),


    /*
    401 UNAUTHORIZED : 인증되지 않은 사용자
    */

    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "만료되었거나 유효하지 않은 토큰입니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다"),


    /*
    404 Not Found
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다");






    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
