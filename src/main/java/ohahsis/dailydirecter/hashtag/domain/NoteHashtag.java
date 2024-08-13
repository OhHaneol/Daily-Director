package ohahsis.dailydirecter.hashtag.domain;

import jakarta.persistence.*;
import lombok.*;
import ohahsis.dailydirecter.note.domain.Note;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "note_hashtag")
public class NoteHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long noteHashtagId;
    @Column(name = "note_id")
    private Long noteId;

    @Column(name = "hashtag_id")
    private Long hashtagId;

}
