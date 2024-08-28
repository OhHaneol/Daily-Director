package ohahsis.dailydirecter.note.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class NoteSaveResponse {

    private Long note_id;
    private List<String> content;
    private Boolean status;
    private String title;
    private List<String> noteHashtagNames;
    private Long user_id;
    private LocalDateTime modifiedAt;
}
