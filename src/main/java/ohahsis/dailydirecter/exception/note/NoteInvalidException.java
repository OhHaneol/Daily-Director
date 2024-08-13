package ohahsis.dailydirecter.exception.note;

import ohahsis.dailydirecter.exception.BusinessException;
import ohahsis.dailydirecter.exception.dto.ErrorType;

public class NoteInvalidException extends BusinessException {

    public NoteInvalidException(ErrorType errorType) {
        super(errorType);
    }
}
