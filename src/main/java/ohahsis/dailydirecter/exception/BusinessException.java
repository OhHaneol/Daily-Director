package ohahsis.dailydirecter.exception;

import lombok.Getter;
import ohahsis.dailydirecter.exception.dto.ErrorType;

// TODO @RequiredArgsConstructor, @AllArgsConstructor 못 쓴 이유가 super() 때문인지 조사
@Getter
public class BusinessException extends RuntimeException {

    // (해결) 예외 작동 시 커스텀 메시지 이외에 스프링 로그까지 전부 전달되는 문제
    //  -> 오류는 정상 response에 담기기 전이니까, 오류 발생 시 클라이언트에 전달되는 데이터를 확인!
    //  -> GlobalExceptionHandler + ErrorDto 관련 = @ExceptionHandler 등록

    private ErrorType errorType;
    private String customMessage;

    public BusinessException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public BusinessException(ErrorType errorType, String customMessage) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.customMessage = customMessage;
    }
}
