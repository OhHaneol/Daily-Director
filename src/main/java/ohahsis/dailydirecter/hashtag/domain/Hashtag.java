package ohahsis.dailydirecter.hashtag.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@Setter
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

//    // TODO 개수 0이면 사라지는 로직 이곳에 추가
//    @Column(name = "cnt")
//    private int cnt;

//    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<NoteHashtag> noteHashtags = new HashSet<>();
    @ElementCollection
    private Set<Long> noteHashtagIds = new HashSet<>();

}
