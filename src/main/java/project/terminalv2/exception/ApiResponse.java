package project.terminalv2.exception;

import jdk.jfr.ContentType;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
public class ApiResponse<T> {

    private String code;
    private String message;
    private T data;

    @Builder
    public ApiResponse(HttpStatus status, String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponse makeResponse(HttpStatus status, String code, String message, T data) {
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }
}
