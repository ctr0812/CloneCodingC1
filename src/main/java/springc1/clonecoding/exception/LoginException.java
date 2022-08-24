package springc1.clonecoding.exception;

import org.springframework.security.core.AuthenticationException;

public class LoginException extends AuthenticationException {
    ErrorCode errorCode;

    public LoginException(String message) {
        super(message);
    }
}
