package com.solution.calc.domain.money.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.solution.calc.api.money.dto.CalculateResponseDto;
import com.solution.calc.api.money.dto.CalculateSearchRequestDto;
import com.solution.calc.api.money.dto.MoneyIndexDto;
import com.solution.calc.constant.CalculateStatus;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.money.entity.CalculateData;
import com.solution.calc.domain.money.entity.QCalculateData;
import com.solution.calc.exception.ServiceLogicException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CalculateQueryDslRepository extends QuerydslRepositorySupport {

    public CalculateQueryDslRepository() {
        super(CalculateData.class);
    }

    QCalculateData calculateData = QCalculateData.calculateData;

    public Page<CalculateResponseDto> findAllCalculate(
            CalculateSearchRequestDto dto,
            Pageable pageable,
            Long officeId,
            UserLevel userLevel
    ) {
        String username = dto.getUsername();
        String nickName = dto.getNickName();
        LocalDateTime startAt = null;
        LocalDateTime endAt = null;
        if (dto.getStartAt() != null && dto.getEndAt() != null) {
            startAt = dto.getStartAt().atStartOfDay();
            endAt = dto.getEndAt().atTime(LocalTime.MAX);
        }
        CalculateStatus calculateStatus = dto.getCalculateStatus();


        JPQLQuery<CalculateResponseDto> query = getCalculateQuery();

        if (username != null) {
            query.where(
                    calculateData.officeUsername.containsIgnoreCase(username)
            );
        }

        if (nickName != null) {
            query.where(
                    calculateData.officeNickName.containsIgnoreCase(nickName)
            );
        }

        if (calculateStatus != null) {
            query.where(
                    calculateData.calculateStatus.eq(calculateStatus)
            );
        }
        if (startAt != null && endAt != null) {
            query.where(
                    calculateData.createAt.goe(startAt)
                            .and(calculateData.createAt.loe(endAt))
            );
        }
        if (!userLevel.equals(UserLevel.ADMIN)) {
            query.where(calculateData.officeId.eq(officeId));
        }

        List<CalculateResponseDto> list = Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.DATA_ACCESS_ERROR))
                .applyPagination(pageable, query)
                .fetch();
        return new PageImpl<>(list, pageable, query.fetchCount());
    }

    public MoneyIndexDto findToday(Long userId, UserLevel userLevel) {
        LocalDateTime today = LocalDateTime.now();
        LocalDate todayDate = LocalDate.now();
        MoneyIndexDto result = MoneyIndexDto.builder().build();
        BigDecimal todayCalculate = BigDecimal.valueOf(0);
        BigDecimal yesterdayCalculate = BigDecimal.valueOf(0);
        JPQLQuery<BigDecimal> todayQuery = from(calculateData)
                .select(calculateData.calculateBalance.sum())
                .where(
                        calculateData.createAt.goe(today.toLocalDate().atStartOfDay())
                                .and(calculateData.createAt.loe(todayDate.atTime(LocalTime.MAX)))
                                .and(calculateData.calculateStatus.eq(CalculateStatus.COMPLETE))
                );
        if (userLevel.equals(UserLevel.OFFICE)) {
            todayQuery.where(calculateData.officeId.eq(userId));
        }
        BigDecimal todayResult = todayQuery.fetchFirst();
        if (todayResult != null) {
            todayCalculate = todayResult;
        }

        JPQLQuery<BigDecimal> yesterdayQuery = from(calculateData)
                .select(calculateData.calculateBalance.sum())
                .where(
                        calculateData.createAt.goe(today.minusDays(1).toLocalDate().atStartOfDay())
                                .and(calculateData.createAt.loe(todayDate.minusDays(1).atTime(LocalTime.MAX)))
                                .and(calculateData.calculateStatus.eq(CalculateStatus.COMPLETE))
                );
        if (userLevel.equals(UserLevel.OFFICE)) {
            yesterdayQuery.where(calculateData.officeId.eq(userId));
        }
        BigDecimal yesterdayResult = yesterdayQuery.fetchFirst();
        if (yesterdayResult != null) {
            yesterdayCalculate = yesterdayResult;
        }
        result.setTodayCalculate(todayCalculate);
        result.setYesterdayCalculate(yesterdayCalculate);
        return result;

    }

    private JPQLQuery<CalculateResponseDto> getCalculateQuery() {
        return from(calculateData)
                .select(
                        Projections.constructor(
                                CalculateResponseDto.class,
                                calculateData.dataId,
                                calculateData.officeId,
                                calculateData.officeUsername,
                                calculateData.officeNickName,
                                calculateData.calculateBank,
                                calculateData.calculateAccount,
                                calculateData.topAdminId,
                                calculateData.topAdminUsername,
                                calculateData.topAdminNickName,
                                calculateData.calculateStatus,
                                calculateData.calculateBalance,
                                calculateData.completeAt,
                                calculateData.createAt
                        )
                );
    }
}
