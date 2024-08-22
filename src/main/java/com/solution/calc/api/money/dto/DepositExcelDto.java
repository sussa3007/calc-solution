package com.solution.calc.api.money.dto;


import com.solution.calc.annotation.ExcelColumn;
import com.solution.calc.constant.DepositStatus;
import com.solution.calc.domain.money.entity.DepositData;
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
public class DepositExcelDto {

    @ExcelColumn(headerName = "NO.")
    private Long dataId;

    @ExcelColumn(headerName = "기업회원 ID")
    private String officeUsername;

    @ExcelColumn(headerName = "기업회원")
    private String officeNickName;

    @ExcelColumn(headerName = "관리자")
    private String topAdminNickName;

    @ExcelColumn(headerName = "회원 식별자")
    private String basicUsername;

    @ExcelColumn(headerName = "입금자명")
    private String basicUserNickName;

    @ExcelColumn(headerName = "은행")
    private String depositBank;

    @ExcelColumn(headerName = "계좌")
    private String depositAccount;

    @ExcelColumn(headerName = "TXNID")
    private String txnId;

    @ExcelColumn(headerName = "처리 상태")
    private DepositStatus depositStatus;

    @ExcelColumn(headerName = "입금액")
    private BigDecimal depositBalance;

    @ExcelColumn(headerName = "처리 완료 시간")
    private LocalDateTime completeAt;

    @ExcelColumn(headerName = "요청 시간")
    private LocalDateTime createAt;

    public static DepositExcelDto of(DepositResponseDto data) {
        return DepositExcelDto.builder()
                .dataId(data.getDataId())
                .officeUsername(data.getOfficeUsername())
                .officeNickName(data.getOfficeNickName())
                .topAdminNickName(data.getTopAdminNickName())
                .basicUsername(data.getBasicUsername())
                .basicUserNickName(data.getBasicUserNickName())
                .depositBank(data.getDepositBank())
                .depositAccount(data.getDepositAccount())
                .txnId(data.getTxnId())
                .depositStatus(data.getDepositStatus())
                .depositBalance(data.getDepositBalance())
                .completeAt(data.getCompleteAt())
                .createAt(data.getCreateAt())
                .build();
    }

}
