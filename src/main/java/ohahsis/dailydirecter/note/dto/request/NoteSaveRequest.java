package ohahsis.dailydirecter.note.dto.request;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class NoteSaveRequest {

//    @NotBlank(message = "내용을 입력해주세요.")
    private List<String> contents;

    private Boolean status;

    private String title;

    private Set<String> hashtagNames;
}
