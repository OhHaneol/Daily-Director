package ohahsis.dailydirecter.note.presentaion;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.auth.model.AuthUser;
import ohahsis.dailydirecter.common.model.ResponseDto;
import ohahsis.dailydirecter.exception.dto.ErrorType;
import ohahsis.dailydirecter.note.application.NoteService;
import ohahsis.dailydirecter.note.dto.request.NoteRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342")
@Tag(name = "노트 관리")
@RestController
@RequestMapping(path = "/api/notes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @Operation(summary = "노트 생성")
    @Parameters({
            @Parameter(name = "contents", required = true),
            @Parameter(name = "status"),
            @Parameter(name = "title"),
            @Parameter(name = "hashtagNames")
    })
    @PostMapping
    public ResponseEntity<?> createNote(
            AuthUser user,
            @RequestBody @Valid NoteRequest request
    ) {
        var response = noteService.writeNote(user, request);
        return ResponseDto.created(response);
    }

    @Operation(summary = "노트 조회")
    @GetMapping("/{id}")
    public ResponseEntity<?> getNote(
            AuthUser user,
            @PathVariable(name = "id") Long id) {
        var response = noteService.getNote(user, id);
        return ResponseDto.ok(response);
    }

    @Operation(summary = "노트 수정")
    @Parameters({
            @Parameter(name = "contents", required = true),
            @Parameter(name = "status"),
            @Parameter(name = "title"),
            @Parameter(name = "hashtagNames")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(
            AuthUser user,
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody NoteRequest request
    ) {
        var response = noteService.editNote(user, id, request);
        return ResponseDto.ok(response);
    }

    @Operation(summary = "노트 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(
            AuthUser user,
            @PathVariable(name = "id") Long id) {
        var response = noteService.deleteNote(user, id);
        return ResponseDto.ok(response);
    }
}
