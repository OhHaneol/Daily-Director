package ohahsis.dailydirecter.home.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ohahsis.dailydirecter.auth.model.AuthUser;
import ohahsis.dailydirecter.common.model.ResponseDto;
import ohahsis.dailydirecter.home.application.SearchService;
import ohahsis.dailydirecter.home.dto.request.SearchRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/home", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class HomeController {
    private final SearchService searchService;

    // (해결) 토큰으로 검증된 사용자라도, 다른 사람의 노트에 접근할 수가 있음.
    //      감독 버전에서는 자신의 노트에만 접근하도록 하는 로직 모듈화가 필요하다.
    //      -> JPA find 메서드에 User 조건을 추가해서 해결!

    /**
     * 노트 검색 - 키워드
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchKeyword(
            AuthUser user,
            @RequestBody @Valid SearchRequest request) {
        var response = searchService.searchKeyword(user, request);
        return ResponseDto.ok(response);
    }

    /** (미)완성 버튼에 따른 노트 목록 */

    // TODO 회원 정보, 새로운 노트 추가 버튼 관련해서 어떤 게 필요한지 조사
}
