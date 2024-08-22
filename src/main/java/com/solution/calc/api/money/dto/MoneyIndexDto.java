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
public class MoneyIndexDto {

    private BigDecimal todayDeposit;

    private BigDecimal yesterdayDeposit;

    private BigDecimal todayCalculate;

    private BigDecimal yesterdayCalculate;

    private BigDecimal todayCommission;

    private BigDecimal yesterdayCommission;


}
