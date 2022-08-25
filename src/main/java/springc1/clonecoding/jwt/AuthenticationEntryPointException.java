package springc1.clonecoding.jwt;

import org.json.simple.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import springc1.clonecoding.exception.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AuthenticationEntryPointException implements
        AuthenticationEntryPoint {


  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException e) throws IOException {


    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    JSONObject responseJson = new JSONObject();
    responseJson.put("error", ErrorCode.INVALID_AUTH_TOKEN.getHttpStatus());
    responseJson.put("code", ErrorCode.INVALID_AUTH_TOKEN.toString());
    responseJson.put("message", ErrorCode.INVALID_AUTH_TOKEN.getErrorMessage());


    response.getWriter().print(responseJson);

  }
}
