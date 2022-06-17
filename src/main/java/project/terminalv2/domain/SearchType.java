package project.terminalv2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum SearchType {

    All(0, "전체"),
    TITLE(1, "제목"),
    WRITER(2, "작성자"),
    CONTENT(3, "내용");

    private Integer code;
    private String key;


    // 요청별 상태 코드 반환
    public static SearchType ofCode(Integer code) {
        return Arrays.stream(values())
                .filter(v -> v.getCode().intValue() == code.intValue())
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("상태 코드 : " + code));
    }

}
