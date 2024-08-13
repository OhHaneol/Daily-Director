package ohahsis.dailydirecter.exception.hashtag;

import ohahsis.dailydirecter.exception.BusinessException;
import ohahsis.dailydirecter.exception.dto.ErrorType;

public class HashtagInvalidException extends BusinessException {
    public HashtagInvalidException(ErrorType errorType) {
        super(errorType);
    }
}
