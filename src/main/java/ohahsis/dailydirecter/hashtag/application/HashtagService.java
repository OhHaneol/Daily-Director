package ohahsis.dailydirecter.hashtag.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.exception.dto.ErrorType;
import ohahsis.dailydirecter.exception.note.NoteInvalidException;
import ohahsis.dailydirecter.hashtag.domain.Hashtag;
import ohahsis.dailydirecter.note.domain.Note;
import ohahsis.dailydirecter.hashtag.domain.NoteHashtag;
import ohahsis.dailydirecter.note.dto.request.NoteRequest;
import ohahsis.dailydirecter.hashtag.infrastructure.HashtagRepository;
import ohahsis.dailydirecter.hashtag.infrastructure.NoteHashtagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static ohahsis.dailydirecter.note.NoteConstants.HASHTAGS_MAX_SIZE;

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
        List<String> savedNoteHashtagNames = new ArrayList<>();

        verifyHashtagByMaxSize(request.getHashtagNames().size());

        // 노트 해시태그 저장
        for (String hashtagName : request.getHashtagNames()) {
            Hashtag hashtag;

            // 기존에 없던 hashtag 는 build
            if (!hashtagRepository.existsByName(hashtagName)) {
                hashtag = buildAndSaveHashtag(hashtagName);
            } else {    // 존재하면 해당 해시태그를 가져옴.
                hashtag = hashtagRepository.findByName(hashtagName);
            }

            // noteHashtag 를 build
            var noteHashtag = NoteHashtag.builder()
                    .note(note)
                    .hashtag(hashtag)
                    .build();
            savedNoteHashtagNames.add(noteHashtag.getHashtag().getName());
            noteHashtagRepository.save(noteHashtag);
        }

        return savedNoteHashtagNames;
    }

    private Hashtag buildAndSaveHashtag(String hashtagName) {
        Hashtag hashtag;
        hashtag = Hashtag.builder()
                .name(hashtagName)
                .build();
        hashtagRepository.save(hashtag);
        return hashtag;
    }

    private void verifyHashtagByMaxSize(int hashtagsSize) {
        if (hashtagsSize > HASHTAGS_MAX_SIZE) {
            throw new NoteInvalidException(
                    ErrorType.HASHTAGS_MAX_SIZE_3);
        }
    }

    public List<String> getHashtagNames(Note note) {

        List<String> noteHashtagNames = new ArrayList<>();

        for (NoteHashtag noteHashtag : note.getNoteHashtags()) {
            noteHashtagNames.add(noteHashtag.getHashtag().getName());
        }
        return noteHashtagNames;
    }

    public List<Long> getNoteIdsByNoteHashtagName(String noteHashtagName) {
        List<Long> findNoteIds = new ArrayList<>();
        List<NoteHashtag> noteHashtags = noteHashtagRepository
                .findByHashtag_NameContaining(noteHashtagName);

        noteHashtags.stream().forEach(
                noteHashtag -> findNoteIds.add(noteHashtag
                        .getNote()
                        .getNoteId()));

        return findNoteIds;
    }

}
