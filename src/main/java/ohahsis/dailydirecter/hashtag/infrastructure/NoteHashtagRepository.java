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
    // NoteHashtag 또한 명확하게 분리된 하나의 기능이기 때문에 별도의 리포지토리를 생성한다.

    // 원하는 키워드를 포함하는 해시태그를, FK 로 갖고 있는 Hashtag 테이블의 Name 필드를 이용해서 찾는다.
    List<NoteHashtag> findByHashtag_NameContaining(String hashtagName);

//    void deleteByNote(Note note);

}
