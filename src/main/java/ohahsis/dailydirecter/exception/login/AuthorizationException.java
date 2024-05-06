package ohahsis.dailydirecter.exception.login;

import ohahsis.dailydirecter.exception.BusinessException;
import ohahsis.dailydirecter.exception.dto.ErrorType;

public class AuthorizationException extends BusinessException {
    public AuthorizationException(ErrorType errorType) {
        super(errorType);
    }
}
