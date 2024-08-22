package com.solution.calc.api.user.dto;

import com.solution.calc.constant.BalanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceRequestDto {

    private Long userId;

    private BigDecimal requestBalance;

    private BalanceStatus requestType;
}
