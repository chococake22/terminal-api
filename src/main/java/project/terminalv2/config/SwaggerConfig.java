package project.terminalv2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;

import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    // Swagger 설정
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .apiInfo(metaDate())
                .directModelSubstitute(Date.class, String.class)
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(LocalDateTime.class, String.class)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()));  // 보안 체계 설정

    }

    // header에 뭘 넣을지 설정함
    private ApiKey apiKey() {           // keyname은 header에서 넘어오는 key값과 일치해야 한다.
        return new ApiKey("JWT", "jwt", "header");
    }

    // 뭐에 대한 API인지 설명하는 부분
    private ApiInfo metaDate() {
        return new ApiInfoBuilder()
                .title("장호원 버스터미널")
                .description("장호원시외버스터미널 API입니다.")
                .version("1.0.0")
                .termsOfServiceUrl("Terms of Service")
                .contact(new Contact("Mr. Kang", "www.naver.com", "fightest21@naver.com"))
                .license("JanghowonTerminal Version 1.0")
                .build();
    }

    // 인증하는 방식 설정
    private SecurityContext securityContext() { // 인증 정보와 인증 주체에 대한 정보를 가지고 있다.
        return springfox
                .documentation.spi.service.contexts.SecurityContext.builder()
                .securityReferences(defaultAuth()).build(); // 아래 설정한 기본 인증 방식을 적용한다.
    }

//   기본 인증 범위 설정
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}
