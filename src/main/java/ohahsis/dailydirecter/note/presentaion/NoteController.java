package ohahsis.dailydirecter.note.presentaion;

import lombok.RequiredArgsConstructor;
import ohahsis.dailydirecter.auth.model.AuthUser;
import ohahsis.dailydirecter.common.model.ResponseDto;
import ohahsis.dailydirecter.note.application.NoteService;
import ohahsis.dailydirecter.note.dto.request.NoteSaveRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/notes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    // TODO @RequestBody 두 개 적용하는 Service 를(ServiceImpl 이용해서) 따로 만들어보자.
    // ArgumentResolver 이용할 수 있다고 함. -> https://stackoverflow.com/questions/27189730/multiple-requestbody-values-in-one-controller-method
//    @PostMapping
//    public ResponseEntity<?> createNote(@RequestBody NoteSaveRequest noteRequest, @RequestBody HashtagSaveRequest hashtagRequest) {
//        var response = noteService.save(noteRequest, hashtagRequest);
//        return ResponseDto.created(response);
//    }

    @PostMapping
    public ResponseEntity<?> createNote(AuthUser user, @RequestBody NoteSaveRequest noteRequest) {
        var response = noteService.save(noteRequest);
        return ResponseDto.created(response);
    }

}
