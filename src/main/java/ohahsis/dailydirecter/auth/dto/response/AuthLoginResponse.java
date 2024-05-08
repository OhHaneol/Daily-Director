package ohahsis.dailydirecter.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthLoginResponse {
    private String nickname;
    private String token;
}