package ohahsis.dailydirecter.config.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ohahsis.dailydirecter.auth.application.TokenService;
import ohahsis.dailydirecter.auth.model.AuthToken;
import ohahsis.dailydirecter.auth.model.AuthUser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static ohahsis.dailydirecter.auth.AuthConstants.AUTH_TOKEN_HEADER_KEY;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenService tokenService;


    // 파라미터 타입에 AuthUser 가 있으면 잡아채서 동작하는 resolver!

    @Override
    public boolean supportsParameter(MethodParameter parameter) {   // 개념: 해당 메서드의 매개변수에 대해, 해당 resolver 가 지원하는 것인지를 체크한다.
        return parameter.getParameterType().equals(AuthUser.class); // 사용: 파라미터가 AuthUser.class 를 가지고 있으면 true 를, 아니면 false 를 반환
    }

    @Override
    public Object resolveArgument(                                  // 개념: 매개변수로 넣어줄 값을 제공한다.
            MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        var httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        var accessToken = httpServletRequest.getHeader(AUTH_TOKEN_HEADER_KEY);  // request 의 헤더에서 토큰을 꺼낸다.

        if (accessToken == null) {  // 헤더에서 토큰 받아오려 했더니 토큰이 없음, 로그인을 하면 무조건 토큰을 주는데, 토큰이 없다는 뜻은... 로그인을 하지 않았다?
            if (parameter.isOptional()) {   // 토큰을 아직 받지 않은 게 아니라 회원정보가 없다는 뜻인가? TODO isOptional() 이 뭐지?
                return null;
            }
            accessToken = "";
        }

        var token = new AuthToken(accessToken);

        // TODO 만약 request 의 헤더에 token 이 없어서 accessToken 이 null 이라 "" 을 넣은 경우, 밑에 tokenService 에서는 AuthUser 객체 반환이 되지 않는 건가?
        //  그럼 이 resolver 는 null 을 반환하고, 그럼 controller 에서 예외가 발생하게 되나?

        return tokenService.getAuthUser(token);                     // 사용: token 을 통해 얻은 AuthUser 객체를 반환한다.
    }
}