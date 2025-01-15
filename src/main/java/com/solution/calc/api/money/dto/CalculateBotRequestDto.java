package com.solution.calc.api.money.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalculateBotRequestDto {

    private String systemId;

    private String officeId;

    private BigDecimal totalDepositAmount;
    private BigDecimal totalCalculateAmount;

    private BigDecimal totalWithdrawAmount;
    private BigDecimal remainingOfficeAmount;

    private String status;

    @QueryProjection
    public CalculateBotRequestDto(
            String systemId,
            String officeId,
            BigDecimal totalDepositAmount,
            BigDecimal totalCalculateAmount,
            BigDecimal totalWithdrawAmount,
            BigDecimal remainingOfficeAmount,
            String status) {
        this.systemId = systemId;
        this.officeId = officeId;
        this.totalDepositAmount = totalDepositAmount;
        this.totalCalculateAmount = totalCalculateAmount;
        this.totalWithdrawAmount = totalWithdrawAmount;
        this.remainingOfficeAmount = remainingOfficeAmount;
        this.status = status;
    }

    public CalculateBotRequestDto() {
    }
}
