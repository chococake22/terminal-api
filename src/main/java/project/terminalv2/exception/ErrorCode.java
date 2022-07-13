package project.terminalv2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode { // 에러 종류

    // 회원 관리
    DUPLICATED_USERID(HttpStatus.BAD_REQUEST, "1001", "이미 아이디가 존재합니다."),
    NOT_EQUAL_PWD(HttpStatus.BAD_REQUEST, "1002", "두 비밀번호가 다릅니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "1003", "해당 회원이 없습니다."),
    WRONG_PWD(HttpStatus.BAD_REQUEST, "1004", "비밀번호가 틀립니다."),
    USER_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "1005", "접근 권한이 없습니다."),
    WRONG_DATA(HttpStatus.BAD_REQUEST, "1006", "데이터를 잘못 입력했습니다."),

    // 게시판 관리
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "2001", "게시글이 없습니다."),
    BOARD_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "2002", "권한이 없습니다."),

    // 댓글 관리,
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "3001", "해당 댓글이 없습니다."),
    COMMENT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "3002", "권한이 없습니다."),

    // 버스 시간표 관리
    NOT_FOUND_BUSTIME(HttpStatus.NOT_FOUND, "4001", "해당 시간표가 없습니다."),
    DUPLICATED_BUSTIME(HttpStatus.BAD_REQUEST, "4002", "이미 시간표가 존재합니다."),

    // 파일 관리
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "5001", "파일이 없습니다."),

    // 토큰 관리
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "6001", "사용할 수 없는 토큰입니다.");
    
    private final HttpStatus status;
    private final String code;
    private String message;

    ErrorCode(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }
}
