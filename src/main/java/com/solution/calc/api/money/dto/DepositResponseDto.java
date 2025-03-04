package com.solution.calc.api.money.dto;


import com.solution.calc.annotation.ExcelColumn;
import com.solution.calc.constant.CalculateStatus;
import com.solution.calc.constant.DepositStatus;
import com.solution.calc.domain.money.entity.DepositData;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "데이터 고유 식별자", defaultValue = "데이터 고유 식별자(시스템 측 관리)")
    private Long dataId;

    @Schema(description = "기업회원 고유 식별자", defaultValue = "기업 회원 고유 식별자(시스템 측 관리)")
    private Long officeId;

    @Schema(description = "기업 회원 계정", defaultValue = "기업 회원 계정")
    private String officeUsername;
    @Schema(description = "기업 회원 닉네임", defaultValue = "기업 회원 닉네임")
    private String officeNickName;
    @Schema(description = "상위 관리자 식별자", defaultValue = "상위 관리자 및 부본사 식별자")
    private Long topAdminId;
    @Schema(description = "상위 관리자 아이디", defaultValue = "상위 관리자 및 부본사 계정")
    private String topAdminUsername;
    @Schema(description = "상위 관리자 닉네임", defaultValue = "상위 관리자 및 부본사 닉네임")
    private String topAdminNickName;
    @Schema(description = "회원 식별자", defaultValue = "회원 식별자(시스템 측 관리)")
    private Long basicUserId;
    @Schema(description = "회원 고유 식별자", defaultValue = "회원 고유 식별자(시스템&업체 측 관리)")
    private String basicUsername;
    @Schema(description = "회원 이름", defaultValue = "회원 실명(입금자 명)")
    private String basicUserNickName;
    @Schema(description = "입금 은행", defaultValue = "입금 은행")
    private String depositBank;
    @Schema(description = "입금 계좌", defaultValue = "입금 계좌")
    private String depositAccount;
    @Schema(description = "업체측 입금 은행", defaultValue = "업체측 입금 은행(회원에게 안내 할 은행)")
    private String officeDepositBank;
    @Schema(description = "업체측 입금 계좌", defaultValue = "업체측 입금 계좌(회원에게 안내 할 계좌)")
    private String officeDepositAccount;
    @Schema(description = "업체측 입금 예금주", defaultValue = "업체측 입금 예금주(회원에게 안내 할 예금주)")
    private String officeDepositName;
    @Schema(description = "트랜잭션 키", defaultValue = "트랜잭션 키(거래 내역 멱등성 관리 고유 키 값)")
    private String txnId;
    @Schema(description = "요청에 대한 시스템 처리 상태", defaultValue = "요청에 대한 시스템 측 처리 상태 / 해당 값으로 분기하여 회원 지급 처리 등 후속 처리 필요(COMPLETE 시 시스템 측 승인 완료)")
    private DepositStatus depositStatus;
    @Schema(description = "입금 요청 금액", defaultValue = "입금 요청 금액")
    private BigDecimal depositBalance;
    @Schema(description = "승인 처리 시각", defaultValue = "승인 처리 시각")
    private LocalDateTime completeAt;
    @Schema(description = "요청 시각", defaultValue = "요청 시각")
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
