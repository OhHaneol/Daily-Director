package ohahsis.dailydirecter.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "인증,인가 관리")
@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인")
    @Parameters({
            @Parameter(name = "email", description = "이메일", required = true),
            @Parameter(name = "password", description = "비밀번호", required = true)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginRequest request) {
        var response = authService.login(request);
        return ResponseDto.ok(response);
    }
}