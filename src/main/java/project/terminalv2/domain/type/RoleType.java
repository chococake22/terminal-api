package project.terminalv2.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RoleType {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    // 요청별 상태 코드 반환
    public static RoleType ofCode(String value) {
        return Arrays.stream(values())
                .filter(v -> v.getValue() == value)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("없는 유저권한입니다."));
    }
}
