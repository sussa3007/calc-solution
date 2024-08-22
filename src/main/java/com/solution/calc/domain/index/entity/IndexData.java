package com.solution.calc.domain.index.entity;


import com.solution.calc.api.index.dto.IndexResponseDto;
import com.solution.calc.api.user.dto.UserResponseDto;
import com.solution.calc.audit.Auditable;
import com.solution.calc.constant.UserLevel;
import com.solution.calc.utils.IndexUtils;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "index_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexData extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel;

    @Column(nullable = false, unique = true)
    private String dateKey;

    // 오늘 입금 금액
    @Column(nullable = false)
    private BigDecimal todayDepositBalance;

    // 오늘 출금 금액
    @Column(nullable = false)
    private BigDecimal todayCalculateBalance;

    // 어제 입금 금액
    @Column(nullable = false)
    private BigDecimal yesterdayDepositBalance;

    // 어제 출금 금액
    @Column(nullable = false)
    private BigDecimal yesterdayCalculateBalance;

    // 오늘 정산 수수료
    @Column(nullable = false)
    private BigDecimal todayCalculateCommission;

    // 어제 정산 수수료
    @Column(nullable = false)
    private BigDecimal yesterdayCalculateCommission;

    // 오늘 기준 현재 잔액
    @Column(nullable = false)
    private BigDecimal todayTotalBalance;

    //전일 마감 잔액
    @Column(nullable = false)
    private BigDecimal yesterdayTotalBalance;

    // 현재 전체 잔액
    @Column(nullable = false)
    private BigDecimal resultCurrentBalance;

    public static IndexData create(String dateKey, IndexData yesterdayData, Long userId, UserResponseDto user) {
        LocalDate now = LocalDate.now();
        int monthValue = now.getMonthValue();
        int year = now.getYear();
        if (yesterdayData != null) {
            IndexResponseDto indexResponseDto = IndexResponseDto.of(yesterdayData);
            BigDecimal resultBalance = BigDecimal.valueOf(0);
            if (user.getUserLevel().equals(UserLevel.ADMIN)) {
                resultBalance = indexResponseDto.getResultCurrentBalance();
            } else {
                resultBalance = user.getBalance();
            }
            return IndexData.builder()
                    .date(now)
                    .year(year)
                    .month(monthValue)
                    .dateKey(dateKey)
                    .userId(userId)
                    .userLevel(user.getUserLevel())
                    .todayDepositBalance(BigDecimal.valueOf(0))
                    .todayCalculateBalance(BigDecimal.valueOf(0))
                    .yesterdayDepositBalance(yesterdayData.getTodayDepositBalance())
                    .yesterdayCalculateBalance(yesterdayData.getTodayCalculateBalance())
                    .todayCalculateCommission(BigDecimal.valueOf(0))
                    .yesterdayCalculateCommission(yesterdayData.getTodayCalculateCommission())
                    .todayTotalBalance(BigDecimal.valueOf(0))
                    .yesterdayTotalBalance(resultBalance)
                    .resultCurrentBalance(resultBalance)
                    .build();
        } else {
            return IndexData.builder()
                    .date(now)
                    .year(year)
                    .month(monthValue)
                    .dateKey(dateKey)
                    .userId(userId)
                    .userLevel(user.getUserLevel())
                    .todayDepositBalance(BigDecimal.valueOf(0))
                    .todayCalculateBalance(BigDecimal.valueOf(0))
                    .yesterdayDepositBalance(BigDecimal.valueOf(0))
                    .yesterdayCalculateBalance(BigDecimal.valueOf(0))
                    .todayCalculateCommission(BigDecimal.valueOf(0))
                    .yesterdayCalculateCommission(BigDecimal.valueOf(0))
                    .todayTotalBalance(BigDecimal.valueOf(0))
                    .yesterdayTotalBalance(BigDecimal.valueOf(0))
                    .resultCurrentBalance(BigDecimal.valueOf(0))
                    .build();
        }

    }

}
