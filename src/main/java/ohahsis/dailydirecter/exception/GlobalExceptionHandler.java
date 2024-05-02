package ohahsis.dailydirecter.exception;

import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.exception.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorDto> handleBusinessException(final BusinessException e) { // 왜 final로?
        log.error("[ERROR] BusinessException -> {}", e.getMessage());
        return ResponseEntity.status(e.getErrorType().getStatus())
                .body(new ErrorDto(e.getErrorType()));  // body 에 ErrorType 을 담은 dto 전달?
    }
}
