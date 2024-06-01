package ohahsis.dailydirecter.note.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.auth.model.AuthUser;
import ohahsis.dailydirecter.common.dto.SuccessResponse;
import ohahsis.dailydirecter.exception.login.AuthLoginException;
import ohahsis.dailydirecter.exception.note.NoteInvalidException;
import ohahsis.dailydirecter.hashtag.application.HashtagService;
import ohahsis.dailydirecter.note.domain.Note;
import ohahsis.dailydirecter.note.dto.request.NoteRequest;
import ohahsis.dailydirecter.note.dto.response.NoteResponse;
import ohahsis.dailydirecter.note.dto.response.NoteSaveResponse;
import ohahsis.dailydirecter.note.infrastructure.NoteRepository;
import ohahsis.dailydirecter.exception.dto.ErrorType;
import ohahsis.dailydirecter.user.domain.User;
import ohahsis.dailydirecter.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static ohahsis.dailydirecter.note.NoteConstants.CONTENTS_MAX_SIZE;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final HashtagService hashtagService;

    /**
     * 노트 생성
     */
    @Transactional
    public NoteSaveResponse writeNote(AuthUser user, NoteRequest request) {

        // 제목과 내용이 모두 없는 경우
        if (request.getContents().isEmpty() && request.getTitle().isBlank()) {
            throw new NoteInvalidException(ErrorType.NOT_BLANK_ERROR);
        }

        // 기승전결 외 5개 이상의 문서 저장 요청이 온 경우
        if (request.getContents().size() > CONTENTS_MAX_SIZE) {
            throw new NoteInvalidException(ErrorType.CONTENTS_MAX_SIZE_4);
        }

        // 노트 작성자
        User noteUser = userRepository.findById(user.getId()).orElseThrow(  // TODO 해당 컨트롤러에 Auth 접근으로써 user 는 이미 확인되었는데, null 일 경우를 꼭 대비해야만 하나?
                () -> new AuthLoginException(ErrorType.AUTHORIZATION_ERROR)
        );

        // 노트 저장
        var note = Note.builder()   // 왜 builder 를 사용하는가? -> setter 는 어디서나 값을 수정할 수 있어서 객체지향적으로 좋지 못하다.
                .contents(request.getContents())
                .status(request.getStatus())
                .title(request.getTitle())
                .user(noteUser)
                .build();

        var savedNote = noteRepository.save(note);

        // TODO Service call Service 해결
        List<String> savedNoteHashtagNames = hashtagService.saveNoteHashtag(note, request);

        return new NoteSaveResponse(
                savedNote.getNoteId(),
                savedNote.getContents(),
                savedNote.getStatus(),
                savedNote.getTitle(),
                savedNoteHashtagNames,
                savedNote.getUser().getId());
    }

    /**
     * 노트 수정
     * (해결?) 문제 1: 노트 작성자 이외의 사람도 노트에 대한 접근 및 수정이 가능함. -> 임시로 id 비교해서 막았는데, 이거면 충분할까? resolver 에서 token 으로 해야 하는 것?
     * 문제 2: 임시로 막은 코드의 중복이 심하다. resolver 등으로 옮기자!
     */
    @Transactional
    public NoteSaveResponse editNote(AuthUser user, Long note_id, NoteRequest request) {

        Note findNote = noteRepository.findById(note_id).orElseThrow(
                () -> new NoteInvalidException(ErrorType.NOTE_NOT_FOUND_ERROR)
        );

        isWriter(user, findNote);

        findNote.setTitle(request.getTitle());
        findNote.setStatus(request.getStatus());
        findNote.setContents(request.getContents());

        List<String> savedNoteHashtagNames = hashtagService.saveNoteHashtag(findNote, request);

        return new NoteSaveResponse(
                findNote.getNoteId(),
                findNote.getContents(),
                findNote.getStatus(),
                findNote.getTitle(),
                savedNoteHashtagNames,
                user.getId()
        );
    }

    /**
     * 노트 하나 읽기
     * (해결) 문제 1: 해시태그를 response 에 보냈는데 보이지 않음. 수정 메서드에서도 마찬가지. -> note 에 같이 설정을 해줘야 함.
     */
    @Transactional(readOnly = true)
    public NoteResponse getNote(AuthUser user, Long note_id) {
        Note findNote = noteRepository.findById(note_id).orElseThrow(
                () -> new NoteInvalidException(ErrorType.NOTE_NOT_FOUND_ERROR)
        );

        // 작성자인지 확인
        isWriter(user, findNote);

        // 해시태그 이름 조회
        List<String> noteHashtagNames = new ArrayList<>();
        // TODO Service call Service
        hashtagService.getHashtagNames(findNote, noteHashtagNames);

        return new NoteResponse(
                findNote.getContents(),
                findNote.getStatus(),
                findNote.getTitle(),
                noteHashtagNames);

    }

    /**
     * 노트 삭제
     * (해결) 문제 1: 영속성 때문인지 deleteById 가 실행되지 않고 있음 -> 영속성... 때문은 아닌 것 같고, 노트와 해시태그를 잘 저장해준 뒤 실행하니 정상 삭제가 되었음.
     * (해결) 문제 2: Note.getUser() 가 null 이 나옴. 맵핑 하면 자연스럽게 해당 로그인으로 수행되는 게 아니었음 -> createNote 에 추가.
     */
    @Transactional
    public SuccessResponse deleteNote(AuthUser user, Long note_id) {
        Note findNote = noteRepository.findById(note_id).orElseThrow(
                () -> new NoteInvalidException(ErrorType.NOTE_NOT_FOUND_ERROR)
        );

        // 작성자인지 확인
        isWriter(user, findNote);

        noteRepository.deleteById(note_id);
//        noteRepository.delete(findNote); // entity 로 삭제하는 건 튜플이 아니라 해당 테이블 전체가 삭제되었음. ???

        return new SuccessResponse("성공적으로 삭제되었습니다.");
    }

    /**
     * note_id argument 를 이용한 외부인 접근 제한 메서드
     */
    private void isWriter(AuthUser user, Note findNote) {
        if(!findNote.getUser().getId().equals(user.getId())) {
            throw new AuthLoginException(ErrorType.AUTHORIZATION_ERROR);
        }
    }
}
