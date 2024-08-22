package com.solution.calc.api.index.dto;


import com.solution.calc.api.money.dto.MoneyIndexDto;
import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.index.entity.IndexData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexResponseDto {

    private Long dataId;

    private LocalDate date;

    private BigDecimal todayDepositBalance;

    private BigDecimal todayCalculateBalance;

    private BigDecimal yesterdayDepositBalance;

    private BigDecimal yesterdayCalculateBalance;

    private BigDecimal todayCalculateCommission;

    private BigDecimal yesterdayCalculateCommission;

    private BigDecimal todayTotalBalance;

    private BigDecimal resultCurrentBalance;

    //전일 마감 잔액
    private BigDecimal yesterdayTotalBalance;

    private BigDecimal monthTotalBalance;

    private List<BasicUserInfo> topBasicUser;

    private LocalDateTime createAt;

    public static IndexResponseDto of(IndexData data) {
        UserLevel userLevel = data.getUserLevel();
        if (userLevel.equals(UserLevel.OFFICE)) {

            return IndexResponseDto.builder()
                    .dataId(data.getDataId())
                    .date(data.getDate())
                    .todayDepositBalance(data.getTodayDepositBalance())
                    .todayCalculateBalance(data.getTodayCalculateBalance())
                    .yesterdayDepositBalance(data.getYesterdayDepositBalance())
                    .yesterdayCalculateBalance(data.getYesterdayCalculateBalance())
                    .todayCalculateCommission(data.getTodayCalculateCommission())
                    .yesterdayCalculateCommission(data.getYesterdayCalculateCommission())
                    .todayTotalBalance(data.getTodayTotalBalance().subtract(data.getTodayCalculateCommission()))
                    .resultCurrentBalance(data.getResultCurrentBalance().add(data.getTodayTotalBalance().subtract(data.getTodayCalculateCommission())))
                    .yesterdayTotalBalance(data.getYesterdayTotalBalance())
                    .monthTotalBalance(BigDecimal.valueOf(0))
                    .topBasicUser(List.of())
                    .createAt(data.getCreateAt())
                    .build();
        } else {
            return IndexResponseDto.builder()
                    .dataId(data.getDataId())
                    .date(data.getDate())
                    .todayDepositBalance(data.getTodayDepositBalance())
                    .todayCalculateBalance(data.getTodayCalculateBalance())
                    .yesterdayDepositBalance(data.getYesterdayDepositBalance())
                    .yesterdayCalculateBalance(data.getYesterdayCalculateBalance())
                    .todayCalculateCommission(data.getTodayCalculateCommission())
                    .yesterdayCalculateCommission(data.getYesterdayCalculateCommission())
                    .todayTotalBalance(data.getTodayTotalBalance())
                    .resultCurrentBalance(data.getResultCurrentBalance().add(data.getTodayTotalBalance()))
                    .yesterdayTotalBalance(data.getYesterdayTotalBalance())
                    .monthTotalBalance(BigDecimal.valueOf(0))
                    .topBasicUser(List.of())
                    .createAt(data.getCreateAt())
                    .build();
        }
    }

    public static IndexResponseDto of(MoneyIndexDto dto, UserLevel userLevel) {
        if (userLevel.equals(UserLevel.OFFICE)) {
            BigDecimal todayTotalBalance = dto.getTodayDeposit().subtract(dto.getTodayCommission()).subtract(dto.getTodayCalculate());
            BigDecimal yesterdayTotalBalance = dto.getYesterdayDeposit().subtract(dto.getYesterdayCommission()).subtract(dto.getYesterdayCalculate());
            return IndexResponseDto.builder()
                    .todayDepositBalance(dto.getTodayDeposit())
                    .todayCalculateBalance(dto.getTodayCalculate())
                    .yesterdayDepositBalance(dto.getYesterdayDeposit())
                    .yesterdayCalculateBalance(dto.getYesterdayCalculate())
                    .todayCalculateCommission(dto.getTodayCommission())
                    .yesterdayCalculateCommission(dto.getYesterdayCommission())
                    .todayTotalBalance(todayTotalBalance)
                    .resultCurrentBalance(todayTotalBalance.add(yesterdayTotalBalance))
                    .yesterdayTotalBalance(yesterdayTotalBalance)
                    .monthTotalBalance(BigDecimal.valueOf(0))
                    .topBasicUser(List.of())
                    .build();
        } else {
            BigDecimal todayTotalBalance = dto.getTodayDeposit().subtract(dto.getTodayCalculate());
            BigDecimal yesterdayTotalBalance = dto.getYesterdayDeposit().subtract(dto.getYesterdayCalculate());
            return IndexResponseDto.builder()
                    .todayDepositBalance(dto.getTodayDeposit())
                    .todayCalculateBalance(dto.getTodayCalculate())
                    .yesterdayDepositBalance(dto.getYesterdayDeposit())
                    .yesterdayCalculateBalance(dto.getYesterdayCalculate())
                    .todayCalculateCommission(dto.getTodayCommission())
                    .yesterdayCalculateCommission(dto.getYesterdayCommission())
                    .todayTotalBalance(todayTotalBalance)
                    .resultCurrentBalance(todayTotalBalance.add(yesterdayTotalBalance))
                    .yesterdayTotalBalance(yesterdayTotalBalance)
                    .monthTotalBalance(BigDecimal.valueOf(0))
                    .topBasicUser(List.of())
                    .build();
        }



    }
}
