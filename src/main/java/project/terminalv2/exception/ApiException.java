package project.terminalv2.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());  // 에러 메시지 출력
        this.errorCode = errorCode;
    }
}
