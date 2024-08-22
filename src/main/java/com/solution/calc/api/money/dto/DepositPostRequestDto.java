package com.solution.calc.api.money.dto;

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

    private String depositUsername;

    private String depositName;

    private String depositBank;

    private String depositAccount;

    private BigDecimal depositBalance;

}
