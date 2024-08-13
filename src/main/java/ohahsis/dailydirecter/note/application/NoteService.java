package ohahsis.dailydirecter.note.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.auth.model.AuthUser;
import ohahsis.dailydirecter.common.dto.SuccessResponse;
import ohahsis.dailydirecter.exception.login.AuthLoginException;
import ohahsis.dailydirecter.exception.note.NoteInvalidException;
import ohahsis.dailydirecter.hashtag.application.HashtagService;
import ohahsis.dailydirecter.hashtag.infrastructure.NoteHashtagRepository;
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
import java.util.function.Predicate;

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
     * TODO : SRP 지키기. 생성하는 게 책임인데, 유효성 조회 생성 모두가 섞여 있음. 이를 쪼개야 함.
     * 1. 메서드 분리
     * 2. 트랜잭션이 필요한 곳에서만 메서드 넣어주기.
     */
    @Transactional
    public NoteSaveResponse writeNote(AuthUser user, NoteRequest request) {

        isTitleAndContentExist(request.getTitle(), request.getContents());
        isContentCntUnderMaxSize(request);

        User noteUser = getWriter(user);

        Note note = getAndBuildNote(request, noteUser);

        var savedNote = noteRepository.save(note);
        // TODO Service call Service
        List<String> savedNoteHashtagNames = hashtagService.saveNoteHashtag(note, request);

        return new NoteSaveResponse(
                savedNote.getNoteId(),
                savedNote.getContents(),
                savedNote.getStatus(),
                savedNote.getTitle(),
                savedNoteHashtagNames,
                savedNote.getUserId(),
                savedNote.getCreatedAt());
    }

    @Transactional
    public NoteSaveResponse editNote(AuthUser user, Long note_id, NoteRequest request) {
        Note findNote = getNoteByNoteId(note_id);

        validateSameWriterById(user, findNote);

        setFindNoteByRequest(request, findNote);

        isTitleAndContentExist(findNote.getTitle(), findNote.getContents());

        List<String> savedNoteHashtagNames = hashtagService.saveNoteHashtag(findNote, request);

        return new NoteSaveResponse(
                findNote.getNoteId(),
                findNote.getContents(),
                findNote.getStatus(),
                findNote.getTitle(),
                savedNoteHashtagNames,
                user.getId(),
                findNote.getModifiedAt()
        );
    }

    private void setFindNoteByRequest(NoteRequest request, Note findNote) {
        findNote.update(request.getTitle(), request.getStatus(), request.getContents());
        // JPA 의 dirty checking 으로 인해 명시적인 save() 는 필요 없다.
    }

    /**
     * 노트 하나 읽기
     */
    @Transactional(readOnly = true)
    public NoteResponse getNote(AuthUser user, Long noteId) {
        Note findNote = getNoteByNoteId(noteId);

        validateSameWriterById(user, findNote);

        // TODO Service call Service
        List<String> noteHashtagNames = hashtagService.getHashtagNames(findNote);

        return new NoteResponse(
                findNote.getContents(),
                findNote.getStatus(),
                findNote.getTitle(),
                noteHashtagNames);
    }

    /**
     * 노트 삭제
     */
    @Transactional
    public SuccessResponse deleteNote(AuthUser user, Long noteId) {
        Note findNote = getNoteByNoteId(noteId);

        validateSameWriterById(user, findNote);

        noteRepository.deleteById(noteId);

        return new SuccessResponse("성공적으로 삭제되었습니다.");
    }

    private Note getNoteByNoteId(Long noteId) {
        Note findNote = noteRepository.findById(noteId).orElseThrow(
                () -> new NoteInvalidException(ErrorType.NOTE_NOT_FOUND_ERROR)
        );
        return findNote;
    }

    private Note getAndBuildNote(NoteRequest request, User noteUser) {
        var note = Note.builder()   // 왜 builder 를 사용하는가? -> setter 는 어디서나 값을 수정할 수 있어서 객체지향적으로 좋지 못하다.
                .contents(request.getContents())
                .status(request.getStatus())
                .title(request.getTitle())
                .userId(noteUser.getId())
                .build();
        return note;
    }

    private User getWriter(AuthUser user) {
        User noteUser = userRepository.findById(user.getId())
                .orElseThrow(  // TODO 해당 컨트롤러에 Auth 접근으로써 user 는 이미 확인되었는데, null 일 경우를 꼭 대비해야만 하나?
                        () -> new AuthLoginException(ErrorType.AUTHORIZATION_ERROR)
                );
        return noteUser;
    }

    private void isContentCntUnderMaxSize(NoteRequest request) {
        if (request.getContents().size() > CONTENTS_MAX_SIZE) {
            throw new NoteInvalidException(ErrorType.CONTENTS_MAX_SIZE_4);
        }
    }

    private void isTitleAndContentExist(String title, List<String> contents) {
        if (title.isBlank() && contents.stream().allMatch(Predicate.isEqual(""))) {
            throw new NoteInvalidException(ErrorType.NOT_BOTH_BLANK_ERROR);
        }
    }

    /**
     * note_id argument 를 이용한 외부인 접근 제한 메서드
     */
    private void validateSameWriterById(AuthUser user, Note findNote) {
        if (!findNote.getUserId().equals(user.getId())) {
            throw new AuthLoginException(ErrorType.AUTHORIZATION_ERROR);
        }
    }
}
