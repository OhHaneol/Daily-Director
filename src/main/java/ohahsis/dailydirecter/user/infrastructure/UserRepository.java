package ohahsis.dailydirecter.user.infrastructure;

import ohahsis.dailydirecter.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
