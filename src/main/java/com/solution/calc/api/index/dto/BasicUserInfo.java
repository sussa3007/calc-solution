package com.solution.calc.api.index.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicUserInfo {

    private String username;

    private String nickName;

    private BigDecimal balance;
}
