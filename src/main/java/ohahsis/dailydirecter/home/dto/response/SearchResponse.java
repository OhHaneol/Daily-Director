package ohahsis.dailydirecter.home.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter // 없으면 HttpMessageConversionException 이 발생함.
@AllArgsConstructor
public class SearchResponse {

    // 해당 키워드가 담긴 제목 또는 내용과, 해당 노트를 조회할 때 필요한 noteId 의 쌍
    private Long noteId;
    private String searchType;
    private String title;
    private List<String> contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
