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


    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    JSONObject responseJson = new JSONObject();
    responseJson.put("status", ErrorCode.INVALID_AUTH_TOKEN.getHttpStatus().value());
    responseJson.put("error", ErrorCode.INVALID_AUTH_TOKEN.getHttpStatus());
    responseJson.put("code", ErrorCode.INVALID_AUTH_TOKEN.toString());
    responseJson.put("message", ErrorCode.INVALID_AUTH_TOKEN.getErrorMessage());
    responseJson.put("timestamp", LocalDateTime.now());

    response.getWriter().print(responseJson);

//    String result = objectMapper.writeValueAsString(ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다!"));
//    response.setContentType("application/json");
//    response.setCharacterEncoding("utf-8");
//    response.getWriter().write(result);

  }
}
