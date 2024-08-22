package com.solution.calc.openapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginDto {

    @Schema(description = "아이디", defaultValue = "기업회원 아이디")
    private String username;

    @Schema(description = "비밀번호", defaultValue = "기업회원 비밀번호")
    private String password;

}