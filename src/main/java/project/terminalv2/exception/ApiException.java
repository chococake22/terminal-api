package project.terminalv2.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private ErrorCode errorCode;

    public ApiException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    protected ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
