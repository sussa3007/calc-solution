package com.solution.calc.api.money.dto;


import com.solution.calc.constant.CalculateStatus;
import com.solution.calc.domain.money.entity.CalculateData;
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
public class CalculateResponseDto {

    private Long dataId;

    private Long officeId;

    private String officeUsername;

    private String officeNickName;

    private String calculateBank;

    private String calculateAccount;

    private Long topAdminId;

    private String topAdminUsername;

    private String topAdminNickName;

    private CalculateStatus calculateStatus;

    private BigDecimal calculateBalance;

    private LocalDateTime completeAt;

    private LocalDateTime createAt;

    public static CalculateResponseDto of(CalculateData data) {
        return CalculateResponseDto.builder()
                .dataId(data.getDataId())
                .officeId(data.getOfficeId())
                .officeUsername(data.getOfficeUsername())
                .officeNickName(data.getOfficeNickName())
                .calculateBank(data.getCalculateBank())
                .calculateAccount(data.getCalculateAccount())
                .topAdminId(data.getTopAdminId())
                .topAdminUsername(data.getTopAdminUsername())
                .topAdminNickName(data.getTopAdminNickName())
                .calculateStatus(data.getCalculateStatus())
                .calculateBalance(data.getCalculateBalance())
                .completeAt(data.getCompleteAt())
                .createAt(data.getCreateAt())
                .build();
    }

}
