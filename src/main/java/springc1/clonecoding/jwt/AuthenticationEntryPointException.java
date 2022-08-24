package springc1.clonecoding.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import springc1.clonecoding.controller.response.ResponseDto;
import springc1.clonecoding.exception.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;


@Component
public class AuthenticationEntryPointException implements
        AuthenticationEntryPoint {


  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException e) throws IOException {

    setResponse(response,ErrorCode.INVALID_AUTH_TOKEN);


//
//    String result = objectMapper.writeValueAsString(ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다!"));
//    response.setContentType("application/json");
//    response.setCharacterEncoding("utf-8");
//    response.getWriter().write(result);


  }

  private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    JSONObject responseJson = new JSONObject();
    responseJson.put("status", errorCode.getHttpStatus().value());
    responseJson.put("error", errorCode.getHttpStatus());
    responseJson.put("code", errorCode.toString());
    responseJson.put("message", errorCode.getErrorMessage());
    responseJson.put("timestamp", LocalDateTime.now());


    response.getWriter().print(responseJson);
  }
}
