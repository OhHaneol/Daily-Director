package ohahsis.dailydirecter.note.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class NoteEditRequest {
    private Set<String> contents;

    private Boolean status;

    private String title;

    private Set<String> hashtagNames;
}
