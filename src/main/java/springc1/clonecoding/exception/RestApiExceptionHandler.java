package springc1.clonecoding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springc1.clonecoding.controller.response.ResponseDto;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseEntity handleApiRequestException(IllegalArgumentException ex) {
        return new ResponseEntity(ResponseDto.fail("BAD_REQUEST",ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = { CustomException.class })
    public ResponseEntity<ErrorResponse> handleApiRequestException(CustomException ex) {
        return ErrorResponse.toResponseEntity(ex.getErrorCode());
    }
}