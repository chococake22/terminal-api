package project.terminalv2.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.terminalv2.domain.User;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.UserRepository;
import project.terminalv2.service.JwtService;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${security.jwt.token.access-token-time}")
    private long accessTokenValidMillisecond;

    @Value("${security.jwt.token.refresh-token-time}")
    private long refreshTokenValidMillisecond;

    // 액세스 토큰 재발급
    public String reCreateAccessToken(String token) {

        // 기존의 리프레시 토큰이 유효한지를 확인
        boolean isValid = jwtService.isValidToken(token);

        // 유효하면 액세스 토큰 재발급
        if (isValid) {

            // 토큰으로 유저 정보 조회
            String userId = jwtService.getSubject(token);

            // 해당 유저가 존재하는지 체크
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));

            String accessToken = jwtService.createToken(userId);
            return accessToken;
        } else {
            throw new ApiException(ErrorCode.EXPIRED_TOKEN);
        }
    }
}
