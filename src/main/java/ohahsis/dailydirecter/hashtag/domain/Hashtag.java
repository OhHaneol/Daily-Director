package ohahsis.dailydirecter.hashtag.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hashtag")
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long hashtagId;

    @Column(name = "name", unique = true)
    private String name;

    // TODO 개수 0이면 사라지는 로직 이곳에 추가

}
