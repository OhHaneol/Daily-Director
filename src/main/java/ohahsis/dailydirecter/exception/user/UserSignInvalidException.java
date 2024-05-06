package ohahsis.dailydirecter.exception.user;

import ohahsis.dailydirecter.exception.BusinessException;
import ohahsis.dailydirecter.exception.dto.ErrorType;

public class UserSignInvalidException extends BusinessException {
    public UserSignInvalidException(ErrorType errorType) {
        super(errorType);       // 부모(BusinessException.class)의 생성자를 호출하는 메서드
    }
}
