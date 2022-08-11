package project.terminalv2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.terminalv2.util.JwtAuthInterceptor;
import project.terminalv2.util.JwtManager;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final JwtManager jwtManager;

    // Interceptor 등록
    // 프론트 작업 중 일부 편의를 위해서 Interceptor 제외 api 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthInterceptor(jwtManager))
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v1/user/login", "/api/v1/user/list", "/api/v1/user", "/swagger-resources/**", "/api/v1/user/**",
                        "/swagger-ui/**", "/api/v1/access-token", "/v2/api-docs", "/api/v1/board/list", "/api/v1/board", "/api/v1/board/**", "/api/v1/board/{boardNo}", "/api/v1/board/search", "/api/v1/bustime/admin");  // 해당 URL은 interceptor가 통과하지 않는다.
    }

    // CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("/*");
    }
}
