package ohahsis.dailydirecter.auth.presentation;

import lombok.RequiredArgsConstructor;
import ohahsis.dailydirecter.auth.application.AuthService;
import ohahsis.dailydirecter.auth.dto.request.AuthLoginRequest;
import ohahsis.dailydirecter.auth.dto.response.AuthLoginResponse;
import ohahsis.dailydirecter.common.model.ResponseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginRequest request) {
        var response = authService.login(request);
        return ResponseDto.ok(response);
    }
}