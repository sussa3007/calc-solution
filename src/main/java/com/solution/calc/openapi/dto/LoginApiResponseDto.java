package com.solution.calc.openapi.dto;

import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "로그인 응답 DTO")
public class LoginApiResponseDto {

    @Schema(description = "AccessToken")
    private String accessToken;

    @Schema(description = "회원 식별자")
    private Long userId;

    @Schema(description = "회원 아이디")
    private String username;

    @Schema(description = "회원 닉네임")
    private String nickname;

    @Schema(description = "회원 머니 잔액")
    private BigDecimal balance;

    @Schema(description = "회원 레벨")
    private UserLevel userLevel;


    public static LoginApiResponseDto of(String accessToken, User user) {
        return LoginApiResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .nickname(user.getNickName())
                .balance(user.getBalance())
                .userLevel(user.getUserLevel())
                .accessToken(accessToken)
                .build();
    }

}