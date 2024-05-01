package ohahsis.dailydirecter.user.infrastructure;

import ohahsis.dailydirecter.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional  // TODO 회원을 생성하는 게 아닌데 왜 원자성이 있어야 하나? 이메일을 가진 것을 조회하다 중간에 오류가 나도 문제 없지 않나?
    Boolean existsByEmail(String email);

    @Transactional
    Boolean existsByNickname(String nickname);

    @Transactional
    Optional<User> findByUsernameAndPassword(String username, String password);
}
