package ohahsis.dailydirecter.hashtag.infrastructure;

import ohahsis.dailydirecter.hashtag.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    @Transactional
    Boolean existsByName(String name);

    Hashtag findByName(String name);
}
