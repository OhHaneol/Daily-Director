package ohahsis.dailydirecter.hashtag.infrastructure;

import ohahsis.dailydirecter.hashtag.domain.NoteHashtag;
import ohahsis.dailydirecter.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface NoteHashtagRepository extends JpaRepository<NoteHashtag, Long> {

    List<NoteHashtag> findByNoteId(Long id);

    List<NoteHashtag> findByHashtagId(Long id);

    void deleteAllByNoteId(Long noteId);

}
