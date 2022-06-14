package project.terminalv2.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.terminalv2.domain.User;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.UserRepository;
import project.terminalv2.service.SecurityService;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserRepository userRepository;
    private final SecurityService securityService;

    private static final long refreshTokenValidMillisecond = 2*1000*60*30;

    // 액세스 토큰 재발급
    public String reCreateAccessToken(String userId ,String token) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));

        boolean isValid = securityService.isValidToken(token);

        if (isValid) {
            String accessToken = securityService.createToken(userId, (2*1000*60));
            return accessToken;
        } else {
            throw new RuntimeException("토큰이 유효하지 않습니다.");
        }
    }

}
