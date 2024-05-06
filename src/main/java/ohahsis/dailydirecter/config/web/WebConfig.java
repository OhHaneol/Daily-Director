package ohahsis.dailydirecter.config.web;

import lombok.RequiredArgsConstructor;
import ohahsis.dailydirecter.config.resolver.UserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final UserArgumentResolver userArgumentResolver;

    // HandlerMethodArgumentResolver 등록을 위해서는 WebMvcConfigurer 를 구현한 클래스가 필요하다.
    //그리고 addArgumentResolvers()라는 메소드를 구현해야 한다.


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .maxAge(3600);
//    }
}
