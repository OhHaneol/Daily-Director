package ohahsis.dailydirecter.note.infrastructure;

import ohahsis.dailydirecter.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface NoteRepository extends JpaRepository<Note, Long> {

    // 로그인한 사용자가 작성한 노트 중에서 조건에 따라 조회

    List<Note> findByUser_IdAndTitleContaining(Long userId, String keyword);

    // TODO List<String> 형식의 데이터에서 조건 적용이 잘 되지 않고 있음. 해결하기.
    List<Note> findByUser_IdAndContentsContaining(Long userId, String keyword);

    Note findByUser_IdAndNoteId(Long userId, Long noteId);

    List<Note> findByUser_IdAndStatus(Long userId, Boolean status);
}
