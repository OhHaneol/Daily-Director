package ohahsis.dailydirecter.home.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SearchRequest {

    @NotBlank(message = "검색어는 공백을 제외한 두 글자 이상의 문자열이 포함되어야 합니다.")
    @Size(min = 2, message = "검색어는 공백을 제외한 두 글자 이상의 문자열이 포함되어야 합니다.")
    private String searchKeyword;

}
