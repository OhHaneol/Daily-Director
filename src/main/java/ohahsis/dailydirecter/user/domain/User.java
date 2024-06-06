package ohahsis.dailydirecter.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ohahsis.dailydirecter.note.domain.Note;
import ohahsis.dailydirecter.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Data               // @toString + @getter + @setter + @RequiredArgsConstructor(생성자 자동 생성) + @EqualsAndHashCode
@Entity             // 클래스 위에 선언하여 이 클래스가 엔티티임을 알려준다. JPA에서 정의된 필드들을 바탕으로 데이터베이스에 테이블을 만들어준다.
@Builder
// 해당 클래스에 해당하는 엔티티 객체를 만들 때 빌더 패턴을 이용해서 만들 수 있도록 지정해주는 어노테이션이다. Board.builder(). {여러가지 필드의 초기값 선언 }. build() 형태로 객체를 만들 수 있다.
@NoArgsConstructor  // 파라미터가 아예없는 기본생성자를 자동으로 만들어준다. Jpa 내부에서 동적으로 객체를 생성해서 작업하는 기능 때문에 필요하다.
@AllArgsConstructor // 선언된 모든 필드를 파라미터로 갖는 생성자를 자동으로 만들어준다.
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    private String password;

    private String username;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @OneToMany(mappedBy = "user")
    private List<Note> notes = new ArrayList<>();

}
