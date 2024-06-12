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

        User findUser = userRepository
                .findById(user.getId())
                .orElseThrow(
                        () -> new UserSignInvalidException(
                                ErrorType.USER_NOT_FOUND_ERROR));

        List<SearchResponse> searchResponseList = new ArrayList<>();

        // 제목이 해당 키워드를 포함하고 있을 경우
        List<Note> notesByTitle = noteRepository
                .findByUser_IdAndTitleContaining(
                        findUser.getId(),
                        searchKeyword);

        addResponse(notesByTitle, searchResponseList, "byTitle");


        // 내용이 해당 키워드를 포함하고 있을 경우
        List<Note> notesByContent = noteRepository
                .findByUser_IdAndContentsContaining(
                        findUser.getId(),
                        searchKeyword);

        addResponse(notesByContent, searchResponseList, "byContent");


        // 해시태그가 해당 키워드를 포함하고 있을 경우
        List<Long> noteIds = new ArrayList<>();
        hashtagService.getNoteByName(searchKeyword, noteIds);
        List<Note> notesByHashtag = new ArrayList<>();

        for (Long nId : noteIds) {
            notesByHashtag.add(
                    noteRepository
                            .findByUser_IdAndNoteId(
                                    findUser.getId(),
                                    nId));
        }

        addResponse(notesByHashtag, searchResponseList, "byHashtag");

        return searchResponseList;
    }

    private void addResponse(List<Note> notes, List<SearchResponse> searchResponseList, String keywordType) {
        notes.stream()
                .forEach(note -> searchResponseList.add(
                        new SearchResponse(
                                note.getNoteId(),
                                keywordType,
                                note.getTitle(),
                                note.getContents())));
    }

}
