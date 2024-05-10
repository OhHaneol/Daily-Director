package ohahsis.dailydirecter.exception.note;

import ohahsis.dailydirecter.exception.BusinessException;
import ohahsis.dailydirecter.exception.dto.ErrorType;

public class NoteSaveInvalidException extends BusinessException {
    public NoteSaveInvalidException(ErrorType errorType) {
        super(errorType);
    }
}
