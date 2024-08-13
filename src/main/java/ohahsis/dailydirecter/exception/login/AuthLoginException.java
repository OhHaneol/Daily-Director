package ohahsis.dailydirecter.exception.login;

import ohahsis.dailydirecter.exception.BusinessException;
import ohahsis.dailydirecter.exception.dto.ErrorType;

public class AuthLoginException extends BusinessException {

    public AuthLoginException(ErrorType errorType) {
        super(errorType);
    }
}
