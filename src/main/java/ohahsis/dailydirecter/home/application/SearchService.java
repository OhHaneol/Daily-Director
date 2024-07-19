package ohahsis.dailydirecter.home.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.auth.model.AuthUser;
import ohahsis.dailydirecter.exception.dto.ErrorType;
import ohahsis.dailydirecter.exception.user.UserSignInvalidException;
import ohahsis.dailydirecter.hashtag.application.HashtagService;
import ohahsis.dailydirecter.home.dto.response.SearchResponse;
import ohahsis.dailydirecter.note.domain.Note;
import ohahsis.dailydirecter.note.infrastructure.NoteRepository;
import ohahsis.dailydirecter.user.domain.User;
import ohahsis.dailydirecter.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final HashtagService hashtagService;

    /**
     * 노트 검색 - 키워드로 조회
     * TODO 추후 노트가 많아지면 지나치게 많은 데이터를 보내야 함. 모든 노트를 반환하지 않고 스크롤에 따른 요청에 맞게 n개를 반환하도록.
     */
    @Transactional(readOnly = true)
    public List<SearchResponse> searchKeyword(
            AuthUser user,
            String searchKeyword) {
        User findUser = findUser(user.getId());

        List<SearchResponse> searchResponseList = new ArrayList<>();

        // 제목이 해당 키워드를 포함하고 있을 경우
        List<Note> notesByTitle = getNotesByTitle(findUser.getId(), searchKeyword);
        searchResponseList.addAll(addResponse(notesByTitle, "byTitle"));

        // 내용이 해당 키워드를 포함하고 있을 경우
        List<Note> notesByContent = getNotesByContent(findUser.getId(), searchKeyword);
        searchResponseList.addAll(addResponse(notesByContent, "byContent"));

        // 해시태그가 해당 키워드를 포함하고 있을 경우
        List<Note> notesByHashtag = getNotesByHashtag(findUser.getId(), searchKeyword);
        searchResponseList.addAll(addResponse(notesByHashtag, "byHashtag"));


        return searchResponseList;
    }

    private User findUser(Long userId) {

        User findUser = userRepository
                .findById(userId)
                .orElseThrow(
                        () -> new UserSignInvalidException(
                                ErrorType.USER_NOT_FOUND_ERROR));
        return findUser;
    }

    private List<Note> getNotesByTitle(Long userId, String searchKeyword) {

        List<Note> notesByTitle = noteRepository
                .findByUser_IdAndTitleContaining(
                        userId,
                        searchKeyword);
        return notesByTitle;
    }

    private List<Note> getNotesByContent(Long userId, String searchKeyword) {

        List<Note> notesByContent = noteRepository
                .findByUser_IdAndContentsContaining(
                        userId,
                        searchKeyword);
        return notesByContent;
    }

    private List<Note> getNotesByHashtag(Long userId, String searchKeyword) {
        // TODO : Service call Service
        List<Long> findNoteIds = hashtagService.getNoteIdsByNoteHashtagName(searchKeyword);

        List<Note> notesByHashtag = new ArrayList<>();

        for (Long nId : findNoteIds) {
            notesByHashtag.add(
                    noteRepository
                            .findByUser_IdAndNoteId(
                                    userId,
                                    nId));
        }
        return notesByHashtag;
    }

    private List<SearchResponse> addResponse(List<Note> notes, String keywordType) {
        List<SearchResponse> searchResponseList = new ArrayList<>();

        notes.stream()
                .forEach(note -> searchResponseList.add(
                        new SearchResponse(
                                note.getNoteId(),
                                keywordType,
                                note.getTitle(),
                                note.getContents(),
                                note.getCreatedAt(),
                                note.getModifiedAt())));

        return searchResponseList;
    }

}
