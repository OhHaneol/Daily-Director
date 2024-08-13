package ohahsis.dailydirecter.note.infrastructure;

import ohahsis.dailydirecter.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserId(Long userId);
}
