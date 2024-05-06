package ohahsis.dailydirecter.user.application;

import lombok.RequiredArgsConstructor;
import ohahsis.dailydirecter.exception.dto.ErrorType;
import ohahsis.dailydirecter.exception.user.UserSignInvalidException;
import ohahsis.dailydirecter.user.domain.User;
import ohahsis.dailydirecter.user.dto.request.UserSignupRequest;
import ohahsis.dailydirecter.user.dto.response.UserSignupResponse;
import ohahsis.dailydirecter.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserSignupResponse signup(UserSignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserSignInvalidException(ErrorType.DUPLICATION_EMAIL_ERROR);
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new UserSignInvalidException(ErrorType.DUPLICATION_NICKNAME_ERROR);
        }

        var user =
                User.builder()
                        .username(request.getUsername())
                        .email(request.getEmail())
                        .nickname(request.getNickname())
                        .password(request.getPassword())
                        .build();

        var savedUser = userRepository.save(user);

        return new UserSignupResponse(savedUser.getId(), savedUser.getNickname());
    }
}
