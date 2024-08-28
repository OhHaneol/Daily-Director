package ohahsis.dailydirecter.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // UserService에서 UserSignupResponse 반환 시 전부 포함하는 생성자 만들기 위함
public class UserSignupResponse {

    private Long id;
    private String nickname;
}
