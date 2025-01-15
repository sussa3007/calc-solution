package com.solution.calc.api.money.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositAlarmBotRequestDto {

    private String systemId;

    private String officeId;

    private String depositNickname;

    private BigDecimal depositAmount;

    private String status;

    public static DepositAlarmBotRequestDto of(
            String systemId,
            String officeId,
            String depositNickname,
            BigDecimal depositAmount,
            String status
    ) {
        return DepositAlarmBotRequestDto.builder()
                .systemId(systemId)
                .officeId(officeId)
                .depositNickname(depositNickname)
                .depositAmount(depositAmount)
                .status(status)
                .build();
    }

}
