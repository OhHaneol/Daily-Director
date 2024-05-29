package ohahsis.dailydirecter.home.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.auth.model.AuthUser;
import ohahsis.dailydirecter.exception.dto.ErrorType;
import ohahsis.dailydirecter.exception.note.NoteInvalidException;
import ohahsis.dailydirecter.exception.user.UserSignInvalidException;
import ohahsis.dailydirecter.home.dto.request.SearchRequest;
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

    /**
     * 노트 검색 - 키워드로 조회
     */
    @Transactional(readOnly = true)
    public List<SearchResponse> searchNote(AuthUser user, SearchRequest request) {

        String searchKeyword = request.getSearchKeyword();
        List<String> findNotes = new ArrayList<>();

        User findUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new UserSignInvalidException(
                        ErrorType.USER_NOT_FOUND_ERROR
                ));

        List<SearchResponse> searchResponseList = new ArrayList<>();

        // 제목이 해당 키워드를 포함하고 있을 경우
        List<Note> notesByTitle = noteRepository
                .findByUser_IdAndTitleContaining(
                        findUser.getId(),
                        searchKeyword);
        notesByTitle.stream()
                .forEach(
                        note -> searchResponseList.add(
                                new SearchResponse(
                                        note.getNote_id(),
                                        "byTitle",
                                        note.getTitle()
                                )
                        ));

        // 내용이 해당 키워드를 포함하고 있을 경우
        // TODO 콘텐츠에서의 검색이 잘 이루어지지 않는 문제 해결하기
        List<Note> notesByContent = noteRepository
                .findByUser_IdAndContentsContaining(
                        findUser.getId(),
                        searchKeyword);
        notesByContent.stream()
                .forEach(
                        note -> searchResponseList.add(
                                new SearchResponse(
                                        note.getNote_id(),
                                        "byContent",
                                        note.getContents()
                                                .stream()
                                                .findFirst()
                                                .filter(
                                                        content ->
                                                                content.contains(searchKeyword))
                                                .orElseThrow(
                                                        () -> new NoteInvalidException(ErrorType.NOTE_NOT_FOUND_ERROR))
                                )
                        ));


        return searchResponseList;
    }



    /**
     * 노트 검색 - 해시태그로 조회
     */
}
