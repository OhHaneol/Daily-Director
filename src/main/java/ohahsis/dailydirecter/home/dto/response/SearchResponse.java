package ohahsis.dailydirecter.home.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter // 없으면 HttpMessageConversionException 이 발생함.
@AllArgsConstructor
public class SearchResponse {

    private Long noteId;
    private String searchType;
    private String title;
    private List<String> contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
