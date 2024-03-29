package project.terminalv2.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum BoardType {

    NOTICE(0, "공지사항"),
    NORMAL(1, "일상"),
    QUESTION(2, "질문"),
    ANSWER(3, "답변"),
    All(4, "전체");

    private Integer code;
    private String key;

    // 요청별 상태 코드 반환
    public static BoardType ofCode(Integer code) {
        return Arrays.stream(values())
                .filter(v -> v.getCode() == code.intValue())
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("상태 코드 : " + code));
    }
}
