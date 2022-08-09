package project.terminalv2.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class ApiExceptionAdvice {   // 예외 종류에 따라 어떤 식으로 처리할 것인지 정하는 곳

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> exceptionHandler(HttpServletRequest request, ApiException e) {
        log.error("ApiException={}", e);

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.builder()
                        .code(e.getErrorCode().getCode())
                        .message(e.getErrorCode().getMessage())
                        .build());
    }

    // 데이터 입력 오류 validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validExceptionHandler(MethodArgumentNotValidException e) {

        // 에러 메시지 출력
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage() + "\n");
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .code(ErrorCode.WRONG_DATA.getCode())
                        .message(sb.toString())
                        .build());
    }
}
