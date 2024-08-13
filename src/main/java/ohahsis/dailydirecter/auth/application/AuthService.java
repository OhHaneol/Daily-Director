package ohahsis.dailydirecter.auth.application;

import lombok.RequiredArgsConstructor;
import ohahsis.dailydirecter.auth.dto.request.AuthLoginRequest;
import ohahsis.dailydirecter.auth.dto.response.AuthLoginResponse;
import ohahsis.dailydirecter.exception.dto.ErrorType;
import ohahsis.dailydirecter.exception.login.AuthLoginException;
import ohahsis.dailydirecter.user.domain.User;
import ohahsis.dailydirecter.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthLoginResponse login(AuthLoginRequest request) {

        var user = getUserByEmail(request.getEmail());

        verifyPassword(request.getPassword(), user.getPassword());

        var token = getTokenByUser(user);

        return new AuthLoginResponse(user.getNickname(), token);
    }


    private User getUserByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthLoginException(ErrorType.USER_NOT_FOUND_ERROR));
        return user;
    }

    private void verifyPassword(String reqPassword, String userPassword) {
        if (!reqPassword.equals(userPassword)) {
            throw new AuthLoginException(ErrorType.INVALID_PASSWORD);
        }
    }

    private String getTokenByUser(User user) {
        return tokenService.jwtBuilder(user.getId(), user.getNickname());
    }
}
