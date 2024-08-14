package ohahsis.dailydirecter.common.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ohahsis.dailydirecter.exception.dto.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import org.springframework.web.ErrorResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDto<T> implements Serializable {

    private boolean success;
    private T data;
    private ErrorResponse error;

    public ResponseDto(T data) {
        this.success = true;
        this.data = data;
        this.error = null;
    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private String code;
        private String message;
    }

    public static <T> ResponseEntity<ResponseDto<T>> ok(T data) {
        return ResponseEntity.ok(new ResponseDto<T>(data));
    }

    public static <T> ResponseEntity<ResponseDto<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<T>(data));
    }

    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public static ResponseEntity<ResponseDto<Void>> error(ErrorType errorType) {
        ResponseDto<Void> responseDto = new ResponseDto<>();
        responseDto.setSuccess(false);
        responseDto.setError(new ErrorResponse(errorType.name(), errorType.getMessage()));
        return ResponseEntity.status(errorType.getStatus()).body(responseDto);
    }

    public static ResponseEntity<ResponseDto<Void>> error(HttpStatus status, String code, String message) {
        ResponseDto<Void> responseDto = new ResponseDto<>();
        responseDto.setSuccess(false);
        responseDto.setError(new ErrorResponse(code, message));
        return ResponseEntity.status(status).body(responseDto);
    }
}