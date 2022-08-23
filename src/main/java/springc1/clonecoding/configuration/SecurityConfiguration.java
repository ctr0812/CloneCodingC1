package springc1.clonecoding.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springc1.clonecoding.jwt.AccessDeniedHandlerException;
import springc1.clonecoding.jwt.AuthenticationEntryPointException;
import springc1.clonecoding.jwt.JwtFilter;
import springc1.clonecoding.jwt.TokenProvider;
import springc1.clonecoding.service.UserDetailsServiceImpl;

import javax.annotation.PostConstruct;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.TimeZone;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfiguration {

    @Value("${jwt.secret}")
    String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AuthenticationEntryPointException authenticationEntryPointException;
    private final AccessDeniedHandlerException accessDeniedHandlerException;

    // JVM 기본 시간대를 변경
    @PostConstruct
    public void start() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // cors 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("POST","GET","DELETE","PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Access_Token");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring().requestMatchers(
                    // js, css, image 등의 파일
                    PathRequest.toStaticResources().atCommonLocations()
            );
        };
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();

        http.csrf().disable()

                .headers()
                .frameOptions().sameOrigin() // SocckJs HTML ifram 요소 전송 허용

                .and()
                  // 시큐리티 예외 처리
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointException)
                .accessDeniedHandler(accessDeniedHandlerException)

                 // jwt 토큰 사용하므로 세션 생성 X
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()

                 // preflight request 허용
                .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                  // 회원가입 관련 filter 통과
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/signup").permitAll()
                .antMatchers("/user/signup/usercheck").permitAll()
                .antMatchers("/user/signup/nickcheck").permitAll()
                  // 전체게시글 조회, 게시글 상세 조회 filter 통과
                .antMatchers(HttpMethod.GET,"/api/post").permitAll()
                .antMatchers(HttpMethod.GET,"/api/post/id/**").permitAll()
                  // 전체상품 조회 , 상품 상세 조회 filter 통과
                .antMatchers(HttpMethod.GET,"/api/product").permitAll()
                .antMatchers(HttpMethod.GET,"/api/product/id/**").permitAll()
                // socket 관련 허용
                .antMatchers("/chat/**").permitAll()
                .antMatchers("/ws-stomp/**").permitAll()
                .mvcMatchers("/webjars/**").permitAll()
                .anyRequest().authenticated()

                .and()
                  // 커스텀한 jwt filter 사용
                .addFilterBefore(new JwtFilter(SECRET_KEY, tokenProvider, userDetailsServiceImpl), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
