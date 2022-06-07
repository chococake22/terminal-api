package project.terminalv2.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiExceptionResponse {

    private int code;
    private String errorMessage;

    public ApiExceptionResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.errorMessage = errorCode.getMessage();
    }


}
