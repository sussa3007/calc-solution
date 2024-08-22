package com.solution.calc.api.user.dto;


import com.solution.calc.constant.UserLevel;
import com.solution.calc.constant.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "기업회원, 에이전시 생성 요청 데이터")
public class UserPatchRequestDto {

    @Schema(description = "회원 고유 아이디",requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "회원 이름",requiredMode = Schema.RequiredMode.REQUIRED)
    private String nickName;

    @Schema(description = "회원 비밀번호",requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "회원 메모, 차단 이유 등",requiredMode = Schema.RequiredMode.REQUIRED)
    private String memo;

    @Schema(description = "회원 상태",requiredMode = Schema.RequiredMode.REQUIRED)
    private UserStatus userStatus;

    @Schema(description = "회원 레벨/등급",requiredMode = Schema.RequiredMode.REQUIRED)
    private UserLevel userLevel;

    @Schema(description = "정산 받을 은행",requiredMode = Schema.RequiredMode.REQUIRED)
    private String calculateBank;

    @Schema(description = "정산 받을 계좌",requiredMode = Schema.RequiredMode.REQUIRED)
    private String calculateAccount;

    @Schema(description = "정산 받을 계좌 에금주",requiredMode = Schema.RequiredMode.REQUIRED)
    private String calculateAccountName;

    @Schema(description = "입금 할 은행",requiredMode = Schema.RequiredMode.REQUIRED)
    private String depositBank;

    @Schema(description = "입금 할 계좌",requiredMode = Schema.RequiredMode.REQUIRED)
    private String depositAccount;

    @Schema(description = "입금 할 계좌 에금주",requiredMode = Schema.RequiredMode.REQUIRED)
    private String depositAccountName;

    @Schema(description = "요율",requiredMode = Schema.RequiredMode.REQUIRED)
    private double commission;

    @Schema(description = "텔레그렘 아이디",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String telegram;

    @Schema(description = "상위 관리자 식별자",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long topAdminId;

    @Schema(description = "상위 관리자의 요율",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private double topAdminCommission;

    @Schema(description = "에이전트 1 식별자",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long agent1Id;

    @Schema(description = "에이전트 1의 요율",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private double agent1Commission;

    @Schema(description = "에이전트 2 식별자",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long agent2Id;

    @Schema(description = "에이전트 2 요율",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private double agent2Commission;

    @Schema(description = "에이전트 3 식별자",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long agent3Id;

    @Schema(description = "에이전트 3 요율",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private double agent3Commission;

}
