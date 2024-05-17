package ohahsis.dailydirecter.note.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ohahsis.dailydirecter.note.domain.Hashtag;
import ohahsis.dailydirecter.note.domain.NoteHashtag;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class NoteSaveResponse {
    private Long note_id;
    private List<String> content;
    private Boolean status;
    private String title;
    private List<String> noteHashtagNames;
    private Long user_id;
}
