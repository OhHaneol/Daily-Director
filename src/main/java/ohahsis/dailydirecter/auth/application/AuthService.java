package ohahsis.dailydirecter.auth.application;

import lombok.RequiredArgsConstructor;
import ohahsis.dailydirecter.auth.dto.request.AuthLoginRequest;
import ohahsis.dailydirecter.auth.dto.response.AuthLoginResponse;
import ohahsis.dailydirecter.exception.dto.ErrorType;
import ohahsis.dailydirecter.exception.login.AuthLoginException;
import ohahsis.dailydirecter.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthLoginResponse login(AuthLoginRequest request) {
        var user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new AuthLoginException(ErrorType.FAIL_TO_LOGIN_ERROR));
        var token = tokenService.jwtBuilder(user.getId(), user.getNickname());

        return new AuthLoginResponse(user.getNickname(), token);
    }
}
