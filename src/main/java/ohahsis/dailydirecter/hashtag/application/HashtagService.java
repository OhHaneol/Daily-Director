package ohahsis.dailydirecter.hashtag.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.hashtag.domain.Hashtag;
import ohahsis.dailydirecter.note.domain.Note;
import ohahsis.dailydirecter.hashtag.domain.NoteHashtag;
import ohahsis.dailydirecter.note.dto.request.NoteRequest;
import ohahsis.dailydirecter.hashtag.infrastructure.HashtagRepository;
import ohahsis.dailydirecter.hashtag.infrastructure.NoteHashtagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashtagService {

    private final NoteHashtagRepository noteHashtagRepository;
    private final HashtagRepository hashtagRepository;

    /**
     * NoteHashtag, Hashtag 저장
     */
    public List<String> saveNoteHashtag(Note note, NoteRequest request) {
        List<NoteHashtag> savedNoteHashtags = new ArrayList<>();
        List<String> savedNoteHashtagNames = new ArrayList<>();

        /**
         * 노트 해시태그 저장
         * TODO request 에 해시태그 없는 경우 처리(다른 것들도... nullable)
         */
        for (String name : request.getHashtagNames()) {
            Hashtag hashtag;
            if (!hashtagRepository.existsByName(name)) {    // 기존에 없던 hashtag 는 build
                hashtag = Hashtag.builder()
                        .name(name)
                        .build();
                hashtagRepository.save(hashtag);
            } else {                                        // 존재하면 해당 해시태그를 가져옴.
                hashtag = hashtagRepository.findByName(name);
            }

            var noteHashtag = NoteHashtag.builder() // noteHashtag 를 build
                    .note(note)
                    .hashtag(hashtag)
                    .build();

            savedNoteHashtags.add(noteHashtag);
            savedNoteHashtagNames.add(noteHashtag.getHashtag().getName());

            noteHashtagRepository.save(noteHashtag);
        }
        note.setNoteHashtags(savedNoteHashtags);

        return savedNoteHashtagNames;
    }

    /**
     * Hashtag 이름 출력
     */
    public void getHashtagNames(Note note, List<String> noteHashtagNames) {

        for (NoteHashtag noteHashtag : note.getNoteHashtags()) {
            noteHashtagNames.add(noteHashtag.getHashtag().getName());
        }

    }

    /**
     * TODO Hashtag 엔터티 관련 개수 관리
     */

}
