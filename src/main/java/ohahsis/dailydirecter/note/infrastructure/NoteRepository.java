package ohahsis.dailydirecter.note.infrastructure;

import ohahsis.dailydirecter.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface NoteRepository extends JpaRepository<Note, Long> {
//    Optional<Note> findByTitle(String title);
}
