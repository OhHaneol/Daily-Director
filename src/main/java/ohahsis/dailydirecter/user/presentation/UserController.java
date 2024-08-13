package ohahsis.dailydirecter.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ohahsis.dailydirecter.user.application.UserService;
import ohahsis.dailydirecter.user.dto.request.UserSignupRequest;
import ohahsis.dailydirecter.user.dto.response.UserSignupResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ohahsis.dailydirecter.common.CrossOriginConstants.CROSS_ORIGIN_ADDRESS;

@CrossOrigin(origins = CROSS_ORIGIN_ADDRESS)
@Tag(name = "사용자 관리")
@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입")
    @Parameters({
            @Parameter(name = "email", description = "이메일", required = true),
            @Parameter(name = "nickname", description = "별명", required = true),
            @Parameter(name = "username", description = "이름", required = true),
            @Parameter(name = "password", description = "비밀번호", required = true)
    })
    @PostMapping
    public UserSignupResponse signup(@RequestBody UserSignupRequest request) {
        UserSignupResponse response = userService.signup(request);  // TODO UserResponse 따로 필요한 이유?
        return response;
    }
}
