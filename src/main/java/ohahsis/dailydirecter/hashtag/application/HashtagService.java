//package ohahsis.dailydirecter.hashtag.application;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import ohahsis.dailydirecter.exception.dto.ErrorType;
//import ohahsis.dailydirecter.exception.note.NoteInvalidException;
//import ohahsis.dailydirecter.hashtag.domain.Hashtag;
//import ohahsis.dailydirecter.note.domain.Note;
//import ohahsis.dailydirecter.hashtag.domain.NoteHashtag;
//import ohahsis.dailydirecter.note.dto.request.NoteRequest;
//import ohahsis.dailydirecter.hashtag.infrastructure.HashtagRepository;
//import ohahsis.dailydirecter.hashtag.infrastructure.NoteHashtagRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static ohahsis.dailydirecter.note.NoteConstants.HASHTAGS_MAX_SIZE;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class HashtagService {
//
//    private final NoteHashtagRepository noteHashtagRepository;
//    private final HashtagRepository hashtagRepository;
//
//    /**
//     * NoteHashtag, Hashtag 저장
//     */
////    public List<String> saveNoteHashtag(Note note, NoteRequest request) {
////        List<String> newHashtagNames = request.getHashtagNames().stream()
////                .collect(Collectors.toList());
////        isReqHashtagCntUnderMaxSize(newHashtagNames.size());
////
////        // 기존 NoteHashtag 엔티티들을 삭제
////        noteHashtagRepository.deleteByNote(note);
////
////        List<NoteHashtag> newNoteHashtags = new ArrayList<>();
////
////        for (String name : newHashtagNames) {
////            Hashtag hashtag = getAndSaveHashtag(name);
//////            NoteHashtag noteHashtag = getAndBuildNoteHashtag(note, hashtag);
////            NoteHashtag noteHashtag = new NoteHashtag();
////            noteHashtag.setNote(note);  // 이 메서드 호출로 양방향 관계가 설정됩니다.
////            noteHashtag.setHashtag(hashtag);  // 이 메서드 호출로 양방향 관계가 설정됩니다.
////            newNoteHashtags.add(noteHashtag);
////        }
////
////        // 최대 3개로 제한
////        while (newNoteHashtags.size() > 3) {
////            newNoteHashtags.remove(newNoteHashtags.size() - 1);
////        }
////
////        noteHashtagRepository.saveAll(newNoteHashtags);
////
////        // 사용되지 않는 Hashtag 정리
////        cleanupUnusedHashtags();
////
////        return newNoteHashtags.stream()
////                .map(nh -> nh.getHashtag().getName())
////                .collect(Collectors.toList());
////    }
////    @Transactional
//    public List<String> saveNoteHashtag(Note note, NoteRequest request) {
//        List<String> newHashtagNames = request.getHashtagNames().stream().collect(Collectors.toList());
//        isReqHashtagCntUnderMaxSize(newHashtagNames.size());
//
//        // 기존 NoteHashtag 엔티티들을 삭제
//        noteHashtagRepository.deleteByNote(note);
//
//        List<NoteHashtag> newNoteHashtags = new ArrayList<>();
//
//        for (String name : newHashtagNames) {
//            Hashtag hashtag = getAndSaveHashtag(name);
//            NoteHashtag noteHashtag = new NoteHashtag();
//            newNoteHashtags.add(noteHashtag);
//        }
//
//        noteHashtagRepository.saveAll(newNoteHashtags);
//
////        cleanupUnusedHashtags();
//
//        return newNoteHashtags.stream()
//                .map(nh -> nh.getHashtag().getName())
//                .collect(Collectors.toList());
//    }
//
////    private void cleanupUnusedHashtags() {
////        List<Hashtag> unusedHashtags = hashtagRepository.findAllUnused();
////        hashtagRepository.deleteAll(unusedHashtags);  // CascadeType.ALL과 orphanRemoval=true 설정으로 인해 연관된 NoteHashtag도 자동 삭제됩니다.
////    }
//
//    private void isReqHashtagCntUnderMaxSize(int hashtagCnt) {
//        if (hashtagCnt > HASHTAGS_MAX_SIZE) {
//            throw new NoteInvalidException(
//                    ErrorType.HASHTAGS_MAX_SIZE_3);
//        }
//    }
//
//    private Hashtag getAndSaveHashtag(String name) {
//        Hashtag hashtag;
//        if (!hashtagRepository.existsByName(name)) {
//            hashtag = getAndSaveNewHashtag(name);
//        } else {
//            hashtag = getExistHashtag(name);
//        }
//        return hashtag;
//    }
//
//    private Hashtag getAndSaveNewHashtag(String name) {
//        Hashtag hashtag;
//        hashtag = Hashtag.builder()
//                .name(name)
//                .build();
//        hashtagRepository.save(hashtag);
//        return hashtag;
//    }
//
//    private Hashtag getExistHashtag(String name) {
//        Hashtag hashtag;
//        hashtag = hashtagRepository.findByName(name);
//        return hashtag;
//    }
//
//    private NoteHashtag getAndBuildNoteHashtag(Note note, Hashtag hashtag) {
//        var noteHashtag = NoteHashtag.builder()
//                .note(note)
//                .hashtag(hashtag)
//                .build();
//        return noteHashtag;
//    }
//
//    /**
//     * Note 로 Hashtag 이름 출력
//     *
//     * @return
//     */
//    public List<String> getHashtagNames(Note note) {
//        List<String> noteHashtagNames = new ArrayList<>();
//        for (NoteHashtag noteHashtag : note.getNoteHashtags()) {
//            noteHashtagNames.add(noteHashtag.getHashtag().getName());
//        }
//        return noteHashtagNames;
//    }
//
//    public List<Long> getNoteIdsByNoteHashtagName(String noteHashtagName) {
//        List<Long> findNoteIds = new ArrayList<>();
//        List<NoteHashtag> noteHashtags = noteHashtagRepository
//                .findByHashtag_NameContaining(noteHashtagName);
//
//        noteHashtags.stream().forEach(
//                noteHashtag -> findNoteIds.add(noteHashtag
//                        .getNote()
//                        .getNoteId()));
//
//        return findNoteIds;
//    }
//
//}
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
import ohahsis.dailydirecter.note.infrastructure.NoteRepository;
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
    private final NoteRepository noteRepository;

    /**
     * NoteHashtag, Hashtag 저장
     */
    public List<String> saveNoteHashtag(Note note, NoteRequest request) {
        List<String> savedNoteHashtagNames = new ArrayList<>();

        isHashtagCntUnderMaxSize(request.getHashtagNames().size());

        // TODO NoteHashtag 의 개수를 3개로 맞추는 것.
        //  1. 비교 후 없는 것만 삭제
        //  2. deleteByNotInHastagIdAndNoteId(List<Long> id)
        // where ! in(2,3,4) and note_id = 22 (새로 요청 들어온 해시태그 제외하고 ㅇ기존 지움)

//        note.deleteAllNoteHashtags();
        note = noteRepository.save(note);


        for (String name : request.getHashtagNames()) {
            Hashtag hashtag = getAndSaveHashtag(name);

            NoteHashtag noteHashtag = getAndBuildNoteHashtag(note, hashtag);


//            note.addNoteHashtag(noteHashtag); // Note와 NoteHashtag 간의 양방향 관계 설정


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

    private Hashtag getAndSaveHashtag(String name) {
        Hashtag hashtag;
        if (!hashtagRepository.existsByName(name)) {
            hashtag = getAndSaveNewHashtag(name);
        } else {
            hashtag = getExistHashtag(name);
        }
        return hashtag;
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