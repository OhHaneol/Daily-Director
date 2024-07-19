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

        isHashtagCntUnderMaxSize(request.getHashtagNames().size());

//        noteHashtagRepository.deleteByNote(note);

        for (String name : request.getHashtagNames()) {
            Hashtag hashtag;

            if (!hashtagRepository.existsByName(name)) {
                hashtag = getAndSaveNewHashtag(name);
            } else {
                hashtag = getExistHashtag(name);
            }

            NoteHashtag noteHashtag = getAndBuildNoteHashtag(note, hashtag);

            savedNoteHashtagNames.add(noteHashtag.getHashtag().getName());
            noteHashtagRepository.save(noteHashtag);
        }

        return savedNoteHashtagNames;
    }

    private void isHashtagCntUnderMaxSize(int hashtagCnt) {
        if (hashtagCnt > HASHTAGS_MAX_SIZE) {
            throw new NoteInvalidException(
                    ErrorType.HASHTAGS_MAX_SIZE_3);
        }
    }

    private Hashtag getAndSaveNewHashtag(String name) {
        Hashtag hashtag;
        hashtag = Hashtag.builder()
                .name(name)

                .build();
        hashtagRepository.save(hashtag);
        return hashtag;
    }

    private Hashtag getExistHashtag(String name) {
        Hashtag hashtag;
        hashtag = hashtagRepository.findByName(name);
        return hashtag;
    }

    private NoteHashtag getAndBuildNoteHashtag(Note note, Hashtag hashtag) {
        var noteHashtag = NoteHashtag.builder()
                .note(note)
                .hashtag(hashtag)
                .build();
        return noteHashtag;
    }

    /**
     * Note 로 Hashtag 이름 출력
     * @return
     */
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
