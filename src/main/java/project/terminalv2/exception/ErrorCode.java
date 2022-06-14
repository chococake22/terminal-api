package project.terminalv2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode { // 에러 종류

    // 회원 관리
    USER_SAVE_SUCCESS(HttpStatus.OK, "S900", "아이디 생성"),
    DUPLICATED_USERID(HttpStatus.BAD_REQUEST, "S901", "이미 아이디가 존재합니다."),
    NOT_EQUAL_PWD(HttpStatus.BAD_REQUEST, "S902", "두 비밀번호가 다릅니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "S903", "해당 회원이 없습니다."),
    WRONG_PWD(HttpStatus.BAD_REQUEST, "S904", "비밀번호가 틀립니다."),

    // 게시판 관리
    BOARD_SAVE_SUCCESS(HttpStatus.OK, "S800", "게시글 생성"),
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "S801", "게시글이 없습니다."),
    BOARD_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "S802", "권한이 없습니다."),

    // 댓글 관리
    COMMENT_SAVE_SUCCESS(HttpStatus.OK, "S700", "댓글 생성"),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "S701", "해당 댓글이 없습니다."),
    COMMENT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "S702", "권한이 없습니다."),

    // 버스 시간표 관리
    BUSTIME_SAVE_SUCCESS(HttpStatus.OK, "S600", "버스 시간표 생성"),
    NOT_FOUND_BUSTIME(HttpStatus.NOT_FOUND, "S601", "해당 시간표가 없습니다."),
    DUPLICATED_BUSTIME(HttpStatus.BAD_REQUEST, "S602", "이미 시간표가 존재합니다."),

    // 파일 관리
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "S501", "파일이 없습니다.");


    private final HttpStatus status;
    private final String code;
    private String message;

    ErrorCode(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }
}
