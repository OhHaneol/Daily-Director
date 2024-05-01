package ohahsis.dailydirecter.exception.user;

import ohahsis.dailydirecter.exception.dto.ErrorType;

public class UserSignInvalidException extends RuntimeException {
    public UserSignInvalidException(ErrorType errorType) {
        super(errorType.getMessage());
    }
}
