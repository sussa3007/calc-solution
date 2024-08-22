package com.solution.calc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String description = """
                API 명세에서 요청시 실제 서버로 요청 되니 테스트 용도로만 사용 바랍니다.\n
                기업회원의 과도한 요청은 서버단에서 차단되니 유의 바랍니다.\n
                테스트 전 관리자에게 문의 후 테스트 바랍니다.
                1. 로그인 요청 후 Access Token 발급
                2. Access Token 헤더에 넣어 입금 데이터 생성 요청
                3. 헤더 키(실제 API 요청시) : Authorization / API 명세 페이지 테스트 시 로그인 요청 후 응답 받은 필드 accessToken 값의 "Bearer "를 제거한 키 값을 Authorize 버튼 누른 후 입력. 
                4. 어드민 화면에서 요청 후 승인 대기 상태 입금 요청 데이터 확인 가능
                """;

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

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

}
