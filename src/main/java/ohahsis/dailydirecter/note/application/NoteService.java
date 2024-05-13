package ohahsis.dailydirecter.note.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.note.domain.Hashtag;
import ohahsis.dailydirecter.note.domain.Note;
import ohahsis.dailydirecter.note.domain.NoteHashtag;
import ohahsis.dailydirecter.note.dto.request.NoteSaveRequest;
import ohahsis.dailydirecter.note.dto.request.NoteUpdateRequest;
import ohahsis.dailydirecter.note.dto.response.NoteSaveResponse;
import ohahsis.dailydirecter.note.infrastructure.HashtagRepository;
import ohahsis.dailydirecter.note.infrastructure.NoteHashtagRepository;
import ohahsis.dailydirecter.note.infrastructure.NoteRepository;
import ohahsis.dailydirecter.exception.dto.ErrorType;
import ohahsis.dailydirecter.exception.note.NoteSaveInvalidException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ohahsis.dailydirecter.note.NoteConstants.CONTENTS_MAX_SIZE;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteHashtagRepository noteHashtagRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional
    public NoteSaveResponse save(NoteSaveRequest request) {
        // 제목과 내용이 모두 없는 경우
        if (request.getContents().isEmpty() || request.getTitle().isBlank()) {
            throw new NoteSaveInvalidException(ErrorType.NOT_BLANK_ERROR);
        }

        // 기승전결 외 5개 이상의 문서 저장 요청이 온 경우
        if (request.getContents().size() > CONTENTS_MAX_SIZE) {
            throw new NoteSaveInvalidException(ErrorType.CONTENTS_MAX_SIZE_4);
        }


        /**
         * 노트 저장
         */
        var note = Note.builder()
                .contents(request.getContents())
                .status(request.getStatus())
                .title(request.getTitle())
                .build();

        var savedNote = noteRepository.save(note);


        // 해시태그가 기존에 존재하지 않으면 저장
        // 추후 해시태그 패키지 분리?
        // TODO request 에 해시태그 없는 경우 처리
        /**
         * 해시태그 저장
         */
        for (String name : request.getHashtagNames()) {
            if(!hashtagRepository.existsByName(name)) {
                var hashtag = Hashtag.builder()
                        .name(name)
                        .build();
                hashtagRepository.save(hashtag);
            }
        }

        /**
         * 노트 해시태그 저장
         */
        // 해시태그 이름을 받아와서 Hashtag 객체를 build
        for (String name : request.getHashtagNames()) {
            Hashtag hashtag = Hashtag.builder()
                    .name(name)
                    .build();
            var noteHashtag = NoteHashtag.builder()
                    .note(note)
                    .hashtag(hashtag)
                    .build();
            noteHashtagRepository.save(noteHashtag);
        }

        return new NoteSaveResponse(savedNote.getNote_id());
    }

    public void update(NoteUpdateRequest request) {

    }

    public void delete(Long note_id) {

    }
}
