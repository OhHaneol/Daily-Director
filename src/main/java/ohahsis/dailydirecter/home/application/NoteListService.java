package ohahsis.dailydirecter.home.application;

import lombok.RequiredArgsConstructor;
import ohahsis.dailydirecter.auth.model.AuthUser;
import ohahsis.dailydirecter.home.dto.response.NoteListResponse;
import ohahsis.dailydirecter.note.domain.Note;
import ohahsis.dailydirecter.note.infrastructure.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteListService {
    private final NoteRepository noteRepository;

    /**
     * 완성 여부에 따라 노트 리스트 반환
     */
    @Transactional(readOnly = true)
    public List<NoteListResponse> getNoteList(AuthUser user, boolean status) {
        List<Note> notesByStatus = noteRepository.findByUser_IdAndStatus(
                user.getId(),
                status);

        List<NoteListResponse> response = new ArrayList<>();

        notesByStatus.stream().forEach(note -> response.add(
                new NoteListResponse(
                        note.getTitle(),
                        note.getContents(),
                        note.getStatus())));

        return response;

    }
}
