package ohahsis.dailydirecter.exception.dto;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Getter
public class ErrorDto implements Serializable {

    private final String message;
    private final String reason;

    public ErrorDto(String message, String reason) {
        this.message = message;
        this.reason = reason;
    }

    public ErrorDto(ErrorType message) {
        log.info("[ErrorType] Name -> {}", message.name());
        this.message = message.name();
        this.reason = message.getMessage();
    }
}
