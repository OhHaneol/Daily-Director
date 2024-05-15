package ohahsis.dailydirecter.note.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.exception.note.NoteInvalidException;
import ohahsis.dailydirecter.note.domain.Hashtag;
import ohahsis.dailydirecter.note.domain.Note;
import ohahsis.dailydirecter.note.domain.NoteHashtag;
import ohahsis.dailydirecter.note.dto.request.NoteEditRequest;
import ohahsis.dailydirecter.note.dto.request.NoteSaveRequest;
import ohahsis.dailydirecter.note.dto.response.NoteSaveResponse;
import ohahsis.dailydirecter.note.infrastructure.HashtagRepository;
import ohahsis.dailydirecter.note.infrastructure.NoteHashtagRepository;
import ohahsis.dailydirecter.note.infrastructure.NoteRepository;
import ohahsis.dailydirecter.exception.dto.ErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static ohahsis.dailydirecter.note.NoteConstants.CONTENTS_MAX_SIZE;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteHashtagRepository noteHashtagRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional
    public NoteSaveResponse writeNote(NoteSaveRequest request) {
        // 제목과 내용이 모두 없는 경우
        if (request.getContents().isEmpty() || request.getTitle().isBlank()) {
            throw new NoteInvalidException(ErrorType.NOT_BLANK_ERROR);
        }

        // 기승전결 외 5개 이상의 문서 저장 요청이 온 경우
        if (request.getContents().size() > CONTENTS_MAX_SIZE) {
            throw new NoteInvalidException(ErrorType.CONTENTS_MAX_SIZE_4);
        }


        /**
         * 노트 저장
         */
        var note = Note.builder()   // TODO 왜 builder 를 사용하는가?
                .contents(request.getContents())
                .status(request.getStatus())
                .title(request.getTitle())
                .build();

        var savedNote = noteRepository.save(note);


        // 해시태그가 기존에 존재하지 않으면 저장
        // TODO 해시태그 패키지 분리, request 에 해시태그 없는 경우 처리
//        /**
//         * 해시태그 저장
//         */
//        for (String name : request.getHashtagNames()) {
//            if(!hashtagRepository.existsByName(name)) {
//                var hashtag = Hashtag.builder()
//                        .name(name)
//                        .build();
//                hashtagRepository.save(hashtag);
//            }
//        }

        /**
         * 노트 해시태그 저장
         */
        // 해시태그 이름을 받아와서 Hashtag 객체를 build
        for (String name : request.getHashtagNames()) {     // TODO 해시태그랑 중복 -> 메서드만 빼내서 해결하는지, 아님 해시태그 테이블 분리 시 해결되는지.
            Hashtag hashtag;
            if (!hashtagRepository.existsByName(name)) {    // 기존에 없던 hashtag 는 build
                hashtag = Hashtag.builder()
                        .name(name)
                        .build();
                hashtagRepository.save(hashtag);
            } else {                                        // 존재하면 해당 해시태그를 가져옴.
                hashtag = hashtagRepository.findByName(name);
            }
            var noteHashtag = NoteHashtag.builder() // noteHashtag 를 build
                    .note(note)
                    .hashtag(hashtag)
                    .build();
            noteHashtagRepository.save(noteHashtag);
        }
        return new NoteSaveResponse(savedNote.getNote_id());
    }

    /**
     * 노트 하나 읽기
     */
//    public Long getNote(Long note_id) {
//
//    }

    /**
     * 노트 수정
     */
    @Transactional
    public Long editNote(Long note_id, NoteEditRequest request) {

        Note findNote = noteRepository.findById(note_id).orElseThrow(
                () -> new NoteInvalidException(ErrorType.NOTE_NOT_FOUND_ERROR)
        );
        findNote.setTitle(request.getTitle());
        findNote.setStatus(request.getStatus());
        findNote.setContents(request.getContents());

        /**
         * 해시태그, 노트 해시태그 저장
         */
        // 해시태그 이름을 받아와서 Hashtag 객체를 build
        // TODO (생성은 괜찮은데) 수정의 경우 탈락되는 해시태그, 노트 해시태그가 있을 수 있음. 해당 로직 적용하기
        // 엔티티 내부 cnt 추가해서~ if(cnt < 1)
        for (String name : request.getHashtagNames()) {
            Hashtag hashtag;
            if (!hashtagRepository.existsByName(name)) {    // 기존에 없던 hashtag 는 build
                hashtag = Hashtag.builder()
                        .name(name)
                        .build();
                hashtagRepository.save(hashtag);
            } else {                                        // 존재하면 해당 해시태그를 가져옴.
                hashtag = hashtagRepository.findByName(name);
            }
            var noteHashtag = NoteHashtag.builder() // noteHashtag 를 build
                    .note(findNote)
                    .hashtag(hashtag)
                    .build();
            noteHashtagRepository.save(noteHashtag);
        }

        return findNote.getNote_id();
    }

    /**
     * 노트 삭제
     */
//    public Long deleteNote(Long note_id) {
//
//    }
}
