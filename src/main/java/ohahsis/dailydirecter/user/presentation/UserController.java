package ohahsis.dailydirecter.user.presentation;

import lombok.RequiredArgsConstructor;
import ohahsis.dailydirecter.user.application.UserService;
import ohahsis.dailydirecter.user.dto.request.UserSignupRequest;
import ohahsis.dailydirecter.user.dto.response.UserSignupResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserSignupResponse signup(@RequestBody UserSignupRequest request) {
        UserSignupResponse response = userService.signup(request);  // TODO UserResponse 따로 필요한 이유?
        return response;    // TODO 왜 ResponseDto 따로 만들어 담아서, ResponseEntity<?>로 반환해야 하는지
    }
}
