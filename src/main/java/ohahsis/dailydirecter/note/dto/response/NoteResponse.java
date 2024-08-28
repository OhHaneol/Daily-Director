package ohahsis.dailydirecter.note.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NoteResponse {

    private List<String> contents;
    private Boolean status;
    private String title;
    private List<String> noteHashtagNames;
}
