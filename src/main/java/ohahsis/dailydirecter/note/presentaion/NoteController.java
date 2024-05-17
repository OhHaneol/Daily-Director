package ohahsis.dailydirecter.note.presentaion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.auth.model.AuthUser;
import ohahsis.dailydirecter.common.model.ResponseDto;
import ohahsis.dailydirecter.note.application.NoteService;
import ohahsis.dailydirecter.note.dto.request.NoteEditRequest;
import ohahsis.dailydirecter.note.dto.request.NoteSaveRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/api/notes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<?> createNote(
            AuthUser user,
            @RequestBody NoteSaveRequest noteRequest) {
        var response = noteService.writeNote(user, noteRequest);
        return ResponseDto.created(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNote(
            AuthUser user,
            @PathVariable(name = "id") Long id) {
        var response = noteService.getNote(user, id);
        return ResponseDto.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(
            AuthUser user,
            @PathVariable(name = "id") Long id,
            @RequestBody NoteEditRequest request
    ) {
        var response = noteService.editNote(user, id, request);
        return ResponseDto.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(
            AuthUser user,
            @PathVariable(name = "id") Long id) {
        var response = noteService.deleteNote(user, id);
        return ResponseDto.ok(response);
    }


    // TODO @RequestBody 두 개 적용하는 Service 를(ServiceImpl 이용해서) 따로 만들어보자.
    // ArgumentResolver 이용할 수 있다고 함. -> https://stackoverflow.com/questions/27189730/multiple-requestbody-values-in-one-controller-method
//    @PostMapping
//    public ResponseEntity<?> createNote(@RequestBody NoteSaveRequest noteRequest, @RequestBody HashtagSaveRequest hashtagRequest) {
//        var response = noteService.save(noteRequest, hashtagRequest);
//        return ResponseDto.created(response);
//    }
}
