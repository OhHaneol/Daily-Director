package ohahsis.dailydirecter.common.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ohahsis.dailydirecter.auth.dto.response.AuthLoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // TODO 뭐를 의미?
public class ResponseDto<T> implements Serializable {   // TODO T 가 뭐지?
    private T data;

    public static <T> ResponseEntity<ResponseDto<T>> ok(T data) {   // TODO 반환값이 뭐지?
        return ResponseEntity.ok(new ResponseDto<T>(data));
    }

    public static <T> ResponseEntity<ResponseDto<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<T>(data));
    }

    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}