package ohahsis.dailydirecter.home.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NoteListRequest {

    @NotNull
    Boolean status;
}
