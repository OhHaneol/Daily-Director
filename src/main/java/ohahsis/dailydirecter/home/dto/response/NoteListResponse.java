package ohahsis.dailydirecter.home.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class NoteListResponse {

    private String title;
    private List<String> contents;
    private Boolean status;
    private Long noteId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
