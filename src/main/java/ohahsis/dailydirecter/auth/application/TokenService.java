package ohahsis.dailydirecter.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import ohahsis.dailydirecter.auth.model.AuthToken;
import ohahsis.dailydirecter.auth.model.AuthUser;
import ohahsis.dailydirecter.exception.dto.ErrorType;
import ohahsis.dailydirecter.exception.login.AuthorizationException;
import ohahsis.dailydirecter.user.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenService {

    private final UserRepository userRepository;
    private String key;
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    // login api 에 적용
    public String jwtBuilder(Long id, String nickname) {
        Claims claims = Jwts.claims();  // TODO claims 가 뭐지? -> jwt 의 payload 로, ~~
        claims.put("nickname", nickname);
        claims.put("uid", id);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                // TODO : 유효기간 설정은 다음 MVC에서 진행한다.
                // .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();
    }

    @Value("${jwt.secret.key}")
    public void getSecretKey(String secretKey) {
        key = secretKey;
    }

    public void verifyToken(String token) {
        if (isTokenBlacklisted(token)) {
            throw new AuthorizationException(ErrorType.TOKEN_BLACKLISTED);
        }
        // 토큰을 파싱해서 해당 토큰을 얻고, 토큰이 만료되었으면 에러 발생시킴
        try {
            Jwts.parser().setSigningKey(key.getBytes()).parseClaimsJws(token);
        } catch (Exception e) {
            if (e.getMessage().contains("JWT expired")) {   // JWT expired : 토큰 만료 에러 처리
                throw new AuthorizationException(ErrorType.AUTHORIZATION_ERROR);
            }
            throw new IllegalArgumentException(
                    ErrorType.NULL_TOKEN.getMessage());  // IllegalArgumentException 은 parseClaimsJws 의 예외 처리
        }

        // TODO token 을 이용한 로그인의 경우 DB 조회를 줄이는 게 장점인데, 밑에서 userRepository 를 통해 DB 조회가 일어난다. 왜지?
        //  예상 : 토큰이 분명하게 존재할(회원이라는 것이 확실한) 경우에만 DB 조회가 일어나기 때문에, token 을 이용하지 않을 경우에 비해 DB 조회가 줄어든다.
        Long uid = getUserIdFromToken(token);   // token 으로 유저의 id 를 찾아서
        if (!userRepository.existsById(uid)) {  // id 로 DB 조회를 통해 존재 여부 확인
            throw new AuthorizationException(ErrorType.AUTHORIZATION_ERROR);
        }
    }

    // login 외 타 api 에서 AuthUser 검증 시 적용
    public AuthUser getAuthUser(AuthToken token) {
        verifyToken(token.getToken());  // 토큰 관련 문제가 생길 경우 error
        var id = getUserIdFromToken(token.getToken());
        var user =
                userRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new AuthorizationException(
                                        ErrorType.AUTHORIZATION_ERROR));   // TODO verifyToken 에서 존재 여부 확인했는데, user 가 반환되지 않는 경우가 있나?
        return new AuthUser(id);
    }

    public Long getUserIdFromToken(
            String token) {  // TODO 대충 보면 jwt 를 파싱해서 body(??)에 담긴 uid 를 반환하는 것 같은데, jwt 의 payload 를 말하는 건지... 잘 모르겠음.
        return Long.valueOf(
                (Integer)
                        Jwts.parser()
                                .setSigningKey(key.getBytes())
                                .parseClaimsJws(token)
                                .getBody()
                                .get("uid"));
    }


    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            verifyToken(token);
            blacklistedTokens.add(token);
            log.info("Token blacklisted: {}", token);
        } catch (Exception e) {
            log.error("Error during logout", e);
            throw new AuthorizationException(ErrorType.AUTHORIZATION_ERROR);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
