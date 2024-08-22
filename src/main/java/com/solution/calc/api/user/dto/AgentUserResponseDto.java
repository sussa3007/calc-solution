package com.solution.calc.api.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentUserResponseDto {

    private LocalDateTime createAt;

    private String officeUsername;

    private String officeNickName;

    private BigDecimal totalDeposit;

    private BigDecimal agentCommission;

    private BigDecimal todayOfficeDeposit;

    private BigDecimal todayAgentCommission;

    private LocalDate currentTime;

}
