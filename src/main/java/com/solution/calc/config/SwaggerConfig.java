package com.solution.calc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String description = """
                API 명세에서 응답값 참고.\n
                기업회원의 과도한 요청은 서버단에서 차단되니 유의 바랍니다.\n
               
                
                - 기업 회원 토큰 요청 후 Access Token 발급
                - Access Token 헤더 필수 - 입금 데이터 생성 요청
                - 헤더 키(API 요청시) : Authorization
                - 파트너 페이지에서 요청 후 승인 대기 상태 입금 요청 데이터 확인 가능(20~40초 마다 릴레이션 요청 권장)
                - basicUsername 업체에서 고유값 관리, 업체별 등록 유저 별 Prefix 관리 권장 ex) officeUser1-myUser77 ..
                """;
        Server product = new Server();
        product.setDescription("product");
        product.setUrl("http://localhost:8080");

        Info info = new Info()
                .version("v0.0.1")
                .title("API Docs")
                .description(description);

        // SecuritySecheme명
        String jwtSchemeName = "JWT Access Token";
        // API 요청헤더에 인증정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        // SecuritySchemes 등록
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식
                        .scheme("Bearer")
                        .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)

        OpenAPI result = new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
        result.setServers(List.of(product));
        return result;
    }

}
