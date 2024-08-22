package com.solution.calc.api.money.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoneyDto {

    private BigDecimal totalDeposit;

    private BigDecimal totalCommission;
}
