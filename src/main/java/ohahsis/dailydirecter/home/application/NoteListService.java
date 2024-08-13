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
        List<Note> notesByStatus = getNotesByStatus(user, status);

        List<NoteListResponse> response = getNoteListResponsesByNoteStatus(notesByStatus);

        return response;
    }

    private List<Note> getNotesByStatus(AuthUser user, boolean status) {
        return noteRepository.findByUserId(user.getId()).stream()
                .filter(n -> n.getStatus().equals(status)).toList();
    }

    private List<NoteListResponse> getNoteListResponsesByNoteStatus(List<Note> notesByStatus) {
        List<NoteListResponse> response = new ArrayList<>();

        notesByStatus.stream().forEach(note -> response.add(
                new NoteListResponse(
                        note.getTitle(),
                        note.getContents(),
                        note.getStatus(),
                        note.getNoteId(),
                        note.getCreatedAt(),
                        note.getModifiedAt())));
        return response;
    }
}
