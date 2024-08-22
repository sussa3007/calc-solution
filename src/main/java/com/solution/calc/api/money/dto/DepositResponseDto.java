package com.solution.calc.api.money.dto;


import com.solution.calc.annotation.ExcelColumn;
import com.solution.calc.constant.CalculateStatus;
import com.solution.calc.constant.DepositStatus;
import com.solution.calc.domain.money.entity.DepositData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositResponseDto {

    private Long dataId;

    private Long officeId;

    private String officeUsername;

    private String officeNickName;

    private Long topAdminId;

    private String topAdminUsername;

    private String topAdminNickName;

    private Long basicUserId;

    private String basicUsername;

    private String basicUserNickName;

    private String depositBank;

    private String depositAccount;

    private String officeDepositBank;

    private String officeDepositAccount;

    private String officeDepositName;

    private String txnId;

    private DepositStatus depositStatus;

    private BigDecimal depositBalance;

    private LocalDateTime completeAt;

    private LocalDateTime createAt;

    public static DepositResponseDto of(DepositData data) {
        return DepositResponseDto.builder()
                .dataId(data.getDataId())
                .officeId(data.getOfficeId())
                .officeUsername(data.getOfficeUsername())
                .officeNickName(data.getOfficeNickName())
                .topAdminId(data.getTopAdminId())
                .topAdminUsername(data.getTopAdminUsername())
                .topAdminNickName(data.getTopAdminNickName())
                .basicUserId(data.getBasicUserId())
                .basicUsername(data.getBasicUsername())
                .basicUserNickName(data.getBasicUserNickName())
                .depositBank(data.getDepositBank())
                .depositAccount(data.getDepositAccount())
                .officeDepositBank(data.getOfficeDepositBank())
                .officeDepositAccount(data.getOfficeDepositAccount())
                .officeDepositName(data.getOfficeDepositName())
                .txnId(data.getTxnId())
                .depositStatus(data.getDepositStatus())
                .depositBalance(data.getDepositBalance())
                .completeAt(data.getCompleteAt())
                .createAt(data.getCreateAt())
                .build();
    }

}
