package com.solution.calc.api.money.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.solution.calc.domain.money.entity.DepositData;
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
public class DepositBotRequestDto {

    private String systemId;

    private String officeId;

    private String txnId;

    private String depositUsername;

    private String depositNickname;

    private String depositAccount;

    private String depositBank;

    private BigDecimal depositAmount;

    private String status;

    public static DepositBotRequestDto of(DepositData data, String systemId) {
        return DepositBotRequestDto.builder()
                .systemId(systemId)
                .officeId(data.getOfficeUsername())
                .txnId(data.getTxnId())
                .depositUsername(data.getBasicUsername())
                .depositNickname(data.getBasicUserNickName())
                .depositAccount(data.getDepositAccount())
                .depositBank(data.getDepositBank())
                .depositAmount(data.getDepositBalance())
                .status(data.getDepositStatus().name())
                .build();
    }
}
