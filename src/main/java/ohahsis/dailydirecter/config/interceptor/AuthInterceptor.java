//package ohahsis.dailydirecter.config.interceptor;
//
//import static ohahsis.dailydirecter.auth.AuthConstants.AUTH_TOKEN_HEADER_KEY;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Slf4j
//@Configuration
//public class AuthInterceptor implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
//            Object handler) throws Exception {
//        String accessToken = request.getHeader(AUTH_TOKEN_HEADER_KEY);
//        log.info("Access token: {}", accessToken);
//
//        try {
//            if (accessToken == null) {
////                response.sendRedirect("/html/user/login.html");
//                response.sendRedirect(request.getContextPath() + "/html/user/login.html");
//                return false;
//            }
//        } catch (Exception e) {
//            log.error("Error during redirect", e);
//            return false;
//        }
//
//        return true;
//    }
//}
