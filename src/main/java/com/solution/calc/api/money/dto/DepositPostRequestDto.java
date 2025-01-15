package com.solution.calc.api.money.dto;

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
public class DepositPostRequestDto {

    @Schema(description = "입금자 고유 식별 아이디", defaultValue = "시스템과 업체에서 관리할 회원 고유 식별 아이디 업체측 랜덤 생성하여 요청시 시스템측 자동 반영 ex) office2-user2")
    private String depositUsername;

    @Schema(description = "입금자 명", defaultValue = "입금자 명")
    private String depositName;

    @Schema(description = "입금 은행", defaultValue = "입금 은행")
    private String depositBank;

    @Schema(description = "입금 계좌", defaultValue = "입금 계좌")
    private String depositAccount;

    @Schema(description = "입금 요청액", defaultValue = "입금 요청 금액")
    private BigDecimal depositBalance;

}
