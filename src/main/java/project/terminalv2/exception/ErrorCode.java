package project.terminalv2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode { // 에러 종류

    SUCCESS(900, "아이디 생성"),

    // 중복된 아이디
    DUPLICATED_USERID(901, "이미 아이디가 존재합니다."),

    // 두 비밀번호 불일치
    NOT_EQUAL_PWD(902, "두 비밀번호가 다릅니다.");

    private final int code;
    private final String message;



}
