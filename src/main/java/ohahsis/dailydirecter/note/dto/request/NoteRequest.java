package ohahsis.dailydirecter.note.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.LinkedHashSet;
import java.util.List;

@Data
public class NoteRequest {

    @Size(min = 1, max = 4, message = "최소 1개, 최대 4개의 콘텐츠는 필수입니다.")
    private List<String> contents;

    @Nullable
    private Boolean status;

    @Nullable
    private String title;

    @Size(max = 3, message = "해시태그는 최대 3개까지 등록이 가능합니다.")
    private LinkedHashSet<String> hashtagNames;
}
