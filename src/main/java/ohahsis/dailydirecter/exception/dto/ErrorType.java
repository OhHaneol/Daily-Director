package ohahsis.dailydirecter.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    CONFLICT_ERROR(HttpStatus.BAD_REQUEST, "예기치 못한 에러가 발생했습니다."),

    // 노트 오류
    NOT_BLANK_ERROR(HttpStatus.BAD_REQUEST, "빈 문자열일 수 없습니다."),
    CONTENTS_MAX_SIZE_4(HttpStatus.BAD_REQUEST, "내부문서는 기, 승, 전, 결 의 4개가 최대입니다."),
    NOTE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "id에 해당하는 노트가 존재하지 않습니다."),
    HASHTAGS_MAX_SIZE_3(HttpStatus.BAD_REQUEST, "해시태그는 최대 3개까지 등록 가능합니다."),
    NOT_BOTH_BLANK_ERROR(HttpStatus.BAD_REQUEST, "제목과 내용 둘 다 빈 문자열일 수 없습니다."),

    // 해시태그 오류
    HASHTAG_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "id에 해당하는 해시태그가 존재하지 않습니다."),

    // 노트 해시태그 오류
    NOTE_HASHTAG_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "id에 해당하는 노트 해시태그가 존재하지 않습니다."),


    // 회원가입 오류
    DUPLICATION_EMAIL_ERROR(HttpStatus.BAD_REQUEST, "중복된 이메일 입니다."),
    DUPLICATION_NICKNAME_ERROR(HttpStatus.BAD_REQUEST, "중복된 닉네임 입니다."),

    // 로그인 오류
    USER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    FAIL_TO_LOGIN_ERROR(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),

    // 토큰 오류
    AUTHORIZATION_ERROR(HttpStatus.UNAUTHORIZED, "인증, 인가 오류"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "해당 토큰은 만료된 토큰입니다."),
    NULL_TOKEN(HttpStatus.UNAUTHORIZED, "access token 이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
