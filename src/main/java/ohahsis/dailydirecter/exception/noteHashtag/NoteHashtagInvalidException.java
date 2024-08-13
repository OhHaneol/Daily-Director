package ohahsis.dailydirecter.exception.noteHashtag;

import ohahsis.dailydirecter.exception.BusinessException;
import ohahsis.dailydirecter.exception.dto.ErrorType;

public class NoteHashtagInvalidException extends BusinessException {
    public NoteHashtagInvalidException(ErrorType errorType) {
        super(errorType); }
}
