package ohahsis.dailydirecter.note.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import ohahsis.dailydirecter.common.entity.BaseEntity;
import ohahsis.dailydirecter.hashtag.domain.Hashtag;
import ohahsis.dailydirecter.hashtag.domain.NoteHashtag;
import ohahsis.dailydirecter.user.domain.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "note")
public class Note extends BaseEntity {

    // Note 와 InnerNote 에 대한 고민
    // InnerNote 를 테이블을 분리할까 고민했는데, 내부 리스트 컬럼으로 둬도 될 것 같다.
    // 해시태그와 달리 외부에서 InnerNote 만 조회할 일은 없을 것 같고, 괜히 연산만 복잡한 듯.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long noteId;    // 필드명에 언더바는 되도록 피하자. jpa 메서드에서 문제가 생긴다.

    private String title;

    private Boolean status; // 노트 완결 여부

    @Size(max = 4)
    @ElementCollection  // 컬렉션 객체임을 jpa 에 알려주는 어노테이션
    private List<String> contents = new ArrayList<>();

    @Column(name = "user_id")
    private Long userId;

    public void update(String title, Boolean status, List<String> contents) {
        this.title = title;
        this.status = status;
        this.contents = contents;
    }
}
