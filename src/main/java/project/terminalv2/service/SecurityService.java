package project.terminalv2.service;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.terminalv2.domain.User;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.UserRepository;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class SecurityService {

    // 이 서버에서 사용하는 서명을 위한 키
    @Value("${security.jwt.token.secret-key}")
    private String SECRET_KEY;

    private final UserRepository userRepository;

    private static final long refreshTokenValidMillisecond = 2*1000*60*30;


    // 액세스 토큰 생성
    public String createToken(String subject, long expTime) {

        if (expTime <= 0) {
            throw new RuntimeException("만료시간이 0보다 커야 한다.");
        }

        // 사용할 알고리즘 설정
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // SECRET_KEY를 byte 형태로 변환
        byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);

        // secret key를 지정한 알고리즘 방식으로 암호화해서 새롭게 암호화된 키를 발급받았다.
        Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());

        // JWT 토큰 생성
        return Jwts.builder()
                .setSubject(subject)    // 토큰에 정보를 담는다. -> 유저에 대한 정보를 넣는다.
                .signWith(signingKey, signatureAlgorithm)   // 새롭게 암호회된 키와 사용한 알고리즘을 설정함
                .setExpiration(new Date(System.currentTimeMillis() + expTime))  // 만료시간 설정
                .compact();
    }

    // 리프레쉬 토큰 생성
    public String createRefreshToken(String subject) {
        Date now = new Date();

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);

        Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(signingKey, signatureAlgorithm)
                .compact();
    }

    // 토큰 검증
    // 토큰을 이용해서 subject 정보를 가져오는 메서드
    // subject 정보는 회원의 아이디나 이름같은 본인이 회원임을 증명하는 정보들이다.
    public String getSubject(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)) // 토큰 해석에 사용할 Secret key를 설정한다.
                .build()
                .parseClaimsJws(token)  // 해석할 토큰을 입력한다.
                .getBody();

        // 만약에 토큰이 해석이 된거면 Claim이 생기고 여기서 subject를 가져올 수 있다.

        return claims.getSubject();
    }

    // 토큰 유효한지 검증하는 메서드
    public boolean isValidToken(String token) {

        System.out.println("isValidToken is : " + token);

        try {
            Claims accessClaims = getClaimsFormToken(token);
            System.out.println("액세스 토큰 만료시간 : " + accessClaims.getExpiration());
            System.out.println("액세스 토큰에 담기 유저 아이디 : " + accessClaims.get("userId"));
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 토큰이다");
            return false;
        } catch (JwtException e) {
            System.out.println("사용할 수 없는 토큰이다.");
            return false;
        } catch (NullPointerException e) {
            System.out.println("토큰이 null이다.");
            return false;
        }
    }


    private Claims getClaimsFormToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
