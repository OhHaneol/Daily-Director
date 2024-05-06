package ohahsis.dailydirecter.auth.model;

import lombok.Data;

@Data
public class AuthUser {
    private final Long id;

    public Long getId() {
        return this.id;
    }
}
