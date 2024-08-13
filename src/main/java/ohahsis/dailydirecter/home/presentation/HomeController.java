package ohahsis.dailydirecter.home.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ohahsis.dailydirecter.auth.model.AuthUser;
import ohahsis.dailydirecter.common.model.ResponseDto;
import ohahsis.dailydirecter.home.application.NoteListService;
import ohahsis.dailydirecter.home.application.SearchService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ohahsis.dailydirecter.common.CrossOriginConstants.CROSS_ORIGIN_ADDRESS;

@CrossOrigin(origins = CROSS_ORIGIN_ADDRESS)
@Tag(name = "홈 화면 관리")
@RestController
@RequestMapping(path = "/api/home", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class HomeController {

    private final SearchService searchService;
    private final NoteListService noteListService;

    // 토큰으로 검증된 사용자라도, 다른 사람의 노트에 접근할 수가 있음.
    // -> 감독 버전에서는 자신의 노트에만 접근하도록 하는 로직 모듈화가 필요하다.
    // -> JPA find 메서드에 User 조건을 추가해서 해결!

    @Operation(summary = "노트 검색")
    @Parameter(name = "searchKeyword", description = "검색어", required = true)
    @GetMapping("/search")
    public ResponseEntity<?> searchKeyword(
            AuthUser user,
            @RequestParam(name = "searchKeyword", required = true) String searchKeyword) {
        var response = searchService.searchKeyword(user, searchKeyword);
        return ResponseDto.ok(response);
    }

    @Operation(summary = "(미)완성 버튼에 따른 노트 목록")
    @Parameter(name = "status", description = "완성 여부", required = true)
    @GetMapping("/notes")
    public ResponseEntity<?> getNotes(
            AuthUser user,
            @RequestParam(name = "status", required = false, defaultValue = "false") boolean status) {
        var response = noteListService.getNoteList(user, status);
        return ResponseDto.ok(response);
    }
}
