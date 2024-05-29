package ohahsis.dailydirecter.note.infrastructure;

import ohahsis.dailydirecter.note.domain.Note;
import ohahsis.dailydirecter.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface NoteRepository extends JpaRepository<Note, Long> {

    // 로그인한 사용자가 작성한 노트 중에서 조건에 따라 조회
    List<Note> findByUser_IdAndTitleContaining(Long userId, String keyword);
    List<Note> findByUser_IdAndContentsContaining(Long userId, String keyword);


}
