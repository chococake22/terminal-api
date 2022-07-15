package project.terminalv2.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.service.JwtService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    // 로그인, 회원가입을 제외한 모든 요청은 이 interceptor를 거쳐간다.
    // 토큰을 인증한 후에 유효한 토큰이어야 요청을 수행할 수 있다.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String givenToken = request.getHeader("jwt");

        // 토큰이 유효한지 검증
        if (jwtService.isValidToken(givenToken)) {
            log.info("jwt = {}", givenToken);
            return HandlerInterceptor.super.preHandle(request, response, handler);
        } else {
            throw new ApiException(ErrorCode.EXPIRED_TOKEN);
        }
    }
}
