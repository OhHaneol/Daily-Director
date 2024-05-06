package ohahsis.dailydirecter.user.dto.request;

import lombok.Data;

@Data
public class UserSignupRequest {
    private String email;
    private String username;
    private String nickname;
    private String password;
}
