package project.terminalv2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode { // 에러 종류

    SUCCESS(HttpStatus.OK, "S900", "아이디 생성"),

    // 중복된 아이디
    DUPLICATED_USERID(HttpStatus.BAD_REQUEST, "S901", "이미 아이디가 존재합니다."),

    // 두 비밀번호 불일치
    NOT_EQUAL_PWD(HttpStatus.BAD_REQUEST, "S902", "두 비밀번호가 다릅니다."),

    // 아이디가 없다
    NOT_FOUND(HttpStatus.NOT_FOUND, "S903", "해당 회원이 없습니다."),

    // 아이디나 비밀번호가 틀립니다.
    NOT_ACCEPTED(HttpStatus.NOT_FOUND, "S904", "비밀번호가 틀립니다."),

    // 게시글이 없습니다.
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "S801", "게시글이 없습니다."),

    // 댓글이 없습니다.
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "S801", "해당 댓글이 없습니다.");



    private final HttpStatus status;
    private final String code;
    private String message;

    ErrorCode(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }
}
