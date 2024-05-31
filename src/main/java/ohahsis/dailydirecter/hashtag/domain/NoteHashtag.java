package ohahsis.dailydirecter.hashtag.domain;

import jakarta.persistence.*;
import lombok.*;
import ohahsis.dailydirecter.note.domain.Note;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "note_hashtag")
public class NoteHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_hashtag_id")
    private Long noteHashtagId;

    // NoteHashtag 여러 개가 Note 에 맵핑됨. 근데 여기서 이 ToOne 인 Note 가 null 이 될 수 있다는 말!
    // 왜?? save 에서 문제가 되나?
    // 다시 생각해보니, NoteHashtag 는 note 에 종속적인 개념임. 해당 필드로 찾아가서 cascade 옵션을 걸어주면 됨!
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "note_id")
    private Note note;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "hashtag_id")
//    @Size(max = 3)
    private Hashtag hashtag;

}
