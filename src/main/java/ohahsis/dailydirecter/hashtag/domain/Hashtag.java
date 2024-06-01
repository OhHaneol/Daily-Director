package ohahsis.dailydirecter.hashtag.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hashtag")
public class Hashtag {

    // hashtag 를 note 에 컬럼으로 추가할 수도 있었음.
    // 하지만 추후 커뮤니티버전에서 확장성을 위해(태그당 조회수 조회) 테이블로 분리했음.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long hashtagId;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    private Set<NoteHashtag> noteHashtags = new HashSet<>();

}
