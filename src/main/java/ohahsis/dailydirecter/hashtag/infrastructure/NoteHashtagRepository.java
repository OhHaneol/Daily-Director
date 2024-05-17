package ohahsis.dailydirecter.hashtag.infrastructure;

import ohahsis.dailydirecter.hashtag.domain.NoteHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface NoteHashtagRepository extends JpaRepository<NoteHashtag, Long> {
    // NoteHashtag 또한 명확하게 분리된 하나의 기능이기 때문에 별도의 리포지토리를 생성한다.
}
