package ohahsis.dailydirecter.exception;

import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.common.dto.FieldInvalidResponse;
import ohahsis.dailydirecter.exception.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorDto> handleBusinessException(
            final BusinessException e) { // 왜 final로?
        log.error("[ERROR] BusinessException -> {}", e.getMessage());
        return ResponseEntity.status(e.getErrorType().getStatus())
                .body(new ErrorDto(e.getErrorType()));  // body 에 ErrorType 을 담은 dto 전달?
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<FieldInvalidResponse> handleFieldException(
            final MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(FieldInvalidResponse.builder()
                        .errorCode(bindingResult.getFieldError().getCode())
                        .errorMessage(bindingResult.getFieldError().getDefaultMessage())
                        .build());
    }

}
