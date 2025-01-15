package com.solution.calc.domain.money.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.solution.calc.api.money.dto.*;
import com.solution.calc.constant.CalculateStatus;
import com.solution.calc.constant.DepositStatus;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.money.entity.CalculateData;
import com.solution.calc.domain.money.entity.DepositData;
import com.solution.calc.domain.money.entity.QCalculateData;
import com.solution.calc.domain.money.entity.QDepositData;
import com.solution.calc.domain.user.entity.QUser;
import com.solution.calc.domain.user.entity.User;
import com.solution.calc.exception.ServiceLogicException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class MoneyRepositoryImpl extends QuerydslRepositorySupport implements MoneyRepository{

    private final CalculateJpaRepository calculateJpaRepository;

    private final DepositJpaRepository depositJpaRepository;

    private final CalculateQueryDslRepository calculateQueryDslRepository;

    private final JPAQueryFactory queryFactory;

    @Value("${SYSTEM_PROPERTY}")
    private String systemProperty;

    public MoneyRepositoryImpl(
            CalculateJpaRepository calculateJpaRepository,
            DepositJpaRepository depositJpaRepository,
            CalculateQueryDslRepository calculateQueryDslRepository, JPAQueryFactory queryFactory
    ) {
        super(DepositData.class);
        this.calculateJpaRepository = calculateJpaRepository;
        this.depositJpaRepository = depositJpaRepository;
        this.calculateQueryDslRepository = calculateQueryDslRepository;
        this.queryFactory = queryFactory;
    }

    QDepositData depositData = QDepositData.depositData;

    @Override
    public DepositData saveDepositData(DepositData data) {
        return depositJpaRepository.save(data);
    }

    @Override
    public CalculateData saveCalculateData(CalculateData data) {
        return calculateJpaRepository.save(data);
    }

    @Override
    public Page<DepositResponseDto> findAllDeposit(
            DepositSearchRequestDto dto,
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

        DepositStatus depositStatus = dto.getDepositStatus();
        JPQLQuery<DepositResponseDto> query = getDepositQuery();
        if (username != null) {
            query.where(
                    depositData.basicUsername.containsIgnoreCase(username)
            );
        }

        if (nickName != null) {
            query.where(
                    depositData.basicUserNickName.containsIgnoreCase(nickName).or(depositData.txnId.containsIgnoreCase(nickName))
            );
        }
        if (startAt != null && endAt != null) {
            query.where(
                    depositData.createAt.goe(startAt)
                            .and(depositData.createAt.loe(endAt))
            );
        }
        if (depositStatus != null) {
            query.where(
                    depositData.depositStatus.eq(depositStatus)
            );
        }
        if (!userLevel.equals(UserLevel.ADMIN)) {
            query.where(depositData.officeId.eq(officeId));
        }
        List<DepositResponseDto> list = Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.DATA_ACCESS_ERROR))
                .applyPagination(pageable, query)
                .fetch();
        return new PageImpl<>(list, pageable, query.fetchCount());
    }

    @Override
    public List<DepositResponseDto> findAllDeposit(DepositExcelSearchRequestDto dto, Long officeId, UserLevel userLevel) {
        JPQLQuery<DepositResponseDto> query = getDepositQuery();
        LocalDateTime startAt = null;
        LocalDateTime endAt = null;
        if (dto.getStartAt() != null && dto.getEndAt() != null) {
            startAt = dto.getStartAt().atStartOfDay();
            endAt = dto.getEndAt().atTime(LocalTime.MAX);
        }

        if (startAt != null && endAt != null) {
            query.where(
                    depositData.createAt.goe(startAt)
                            .and(depositData.createAt.loe(endAt))
            );
        } else {
            query.where(
                    depositData.createAt.goe(LocalDateTime.now().toLocalDate().minusDays(1).atStartOfDay())
                            .and(depositData.createAt.loe(LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX)))
            );
        }

        if (!userLevel.equals(UserLevel.ADMIN)) {
            query.where(depositData.officeId.eq(officeId));
        }
        return Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.DATA_ACCESS_ERROR))
                .applySorting(Sort.by("createAt").descending(), query)
                .fetch();
    }

    @Override
    public Page<DepositResponseDto> findAllByBasicUser(String basicUsername, Pageable pageable) {
        JPQLQuery<DepositResponseDto> query = getDepositQuery();

        if (basicUsername != null) {
            query.where(
                    depositData.basicUsername.containsIgnoreCase(basicUsername)
            );
        }
        List<DepositResponseDto> list = Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.DATA_ACCESS_ERROR))
                .applyPagination(pageable, query)
                .fetch();
        return new PageImpl<>(list, pageable, query.fetchCount());
    }

    @Override
    public Page<DepositResponseDto> findLiveDeposit(Pageable pageable, Long officeId, UserLevel userLevel) {
        JPQLQuery<DepositResponseDto> query = getDepositQuery();
        query.where(
                depositData.depositStatus.eq(DepositStatus.DEPOSIT_COMPLETE)
                        .or(depositData.depositStatus.eq(DepositStatus.WAIT))
        );
        if (!userLevel.equals(UserLevel.ADMIN)) {
            query.where(depositData.officeId.eq(officeId));
        }
        List<DepositResponseDto> list = Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.DATA_ACCESS_ERROR))
                .applyPagination(pageable, query)
                .fetch();
        return new PageImpl<>(list, pageable, query.fetchCount());

    }

    @Override
    public DepositData findDeposit(Long dataId) {
        return depositJpaRepository.findById(dataId)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND));
    }

    @Override
    public DepositData findDepositByIdForUpdate(Long dataId) {
        return depositJpaRepository.findByDataId(dataId)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND));
    }

    @Override
    public DepositData findDepositByTx(String txnId) {
        return depositJpaRepository.findByTxnId(txnId)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND));
    }

    @Override
    public Optional<DepositResponseDto> findDepositByUsernameAndStatus(String username) {
        JPQLQuery<DepositResponseDto> query = getDepositQuery();
        if (username != null) {
            query.where(
                    depositData.basicUsername.containsIgnoreCase(username)
            );
        }
        query.where(
                depositData.depositStatus.eq(DepositStatus.DEPOSIT_COMPLETE)
                        .or(depositData.depositStatus.eq(DepositStatus.WAIT))
        );
        return Optional.ofNullable(query.fetchFirst());
    }

    @Override
    public CalculateData findCalculate(Long dataId) {
        return calculateJpaRepository.findById(dataId)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND));
    }

    @Override
    public Optional<CalculateData> findCalculate(Long officeId, CalculateStatus status) {
        return calculateJpaRepository.findByOfficeIdAndCalculateStatus(officeId, status);
    }

    @Override
    public DepositData findDepositByBalanceAndName(String username, BigDecimal balance) {
        return depositJpaRepository.findByBasicUserNickNameAndDepositBalanceAndDepositStatus(username, balance, DepositStatus.WAIT)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND));
    }

    @Override
    public Page<CalculateResponseDto> findAllCalculate(
            CalculateSearchRequestDto dto,
            Pageable pageable,
            Long officeId,
            UserLevel userLevel
    ) {
        return calculateQueryDslRepository.findAllCalculate(
                dto, pageable, officeId, userLevel
        );
    }

    @Override
    public MoneyIndexDto findToday(Long userId, UserLevel userLevel) {
        LocalDateTime today = LocalDateTime.now();
        LocalDate todayDate = LocalDate.now();

        JPQLQuery<MoneyDto> todayQuery = getDepositTodayQuery()
                .where(
                        depositData.createAt.goe(today.toLocalDate().atStartOfDay())
                                .and(depositData.createAt.loe(todayDate.atTime(LocalTime.MAX)))
                                .and(depositData.depositStatus.eq(DepositStatus.COMPLETE))
                );
        if (userLevel.equals(UserLevel.OFFICE)) {
            todayQuery.where(depositData.officeId.eq(userId));
        }
        MoneyDto todayDeposit = todayQuery.fetchFirst();

        JPQLQuery<MoneyDto> yesterdayQuery = getDepositTodayQuery()
                .where(depositData.createAt.goe(today.minusDays(1).toLocalDate().atStartOfDay())
                        .and(depositData.createAt.loe(todayDate.minusDays(1).atTime(LocalTime.MAX)))
                        .and(depositData.depositStatus.eq(DepositStatus.COMPLETE))
                );
        if (userLevel.equals(UserLevel.OFFICE)) {
            yesterdayQuery.where(depositData.officeId.eq(userId));
        }
        MoneyDto yesterdayDeposit = yesterdayQuery.fetchFirst();
        if (todayDeposit.getTotalDeposit() != null && todayDeposit.getTotalCommission() != null) {
            MoneyIndexDto todayDto = calculateQueryDslRepository.findToday(userId, userLevel);
            todayDto.setTodayDeposit(todayDeposit.getTotalDeposit());
            todayDto.setTodayCommission(todayDeposit.getTotalCommission());
            if (yesterdayDeposit.getTotalDeposit() != null) {
                todayDto.setYesterdayDeposit(yesterdayDeposit.getTotalDeposit());
                todayDto.setYesterdayCommission(yesterdayDeposit.getTotalCommission());
            } else {
                todayDto.setYesterdayDeposit(BigDecimal.valueOf(0));
                todayDto.setYesterdayCommission(BigDecimal.valueOf(0));
            }
            return todayDto;
        } else {
            MoneyIndexDto todayDto = calculateQueryDslRepository.findToday(userId, userLevel);
            todayDto.setTodayDeposit(BigDecimal.valueOf(0));
            todayDto.setTodayCommission(BigDecimal.valueOf(0));
            if (yesterdayDeposit.getTotalDeposit() != null) {
                todayDto.setYesterdayDeposit(yesterdayDeposit.getTotalDeposit());
                todayDto.setYesterdayCommission(yesterdayDeposit.getTotalCommission());
            } else {
                todayDto.setYesterdayDeposit(BigDecimal.valueOf(0));
                todayDto.setYesterdayCommission(BigDecimal.valueOf(0));
            }
            return todayDto;
        }
    }

    @Override
    public boolean isFirstDeposit(String basicUsername) {
        return getDepositQuery().where(
                depositData.basicUsername.eq(basicUsername)
                        .and(depositData.depositStatus.eq(DepositStatus.COMPLETE))
        ).fetchFirst() == null;
    }

    private JPQLQuery<MoneyDto> getDepositTodayQuery() {
        return from(depositData)
                .select(
                        Projections.constructor(
                                MoneyDto.class,
                                depositData.depositBalance.sum(),
                                depositData.resultCommission.sum()
                        )
                );
    }

    @Override
    public List<CalculateBotRequestDto> findAllCalculate() {
        QUser user = QUser.user;
        QDepositData depositData = QDepositData.depositData;
        QCalculateData calculateData = QCalculateData.calculateData;

        // 예: 오늘 날짜 필터용 (LocalDateTime now = LocalDateTime.now();)
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        // office 리스트 준비 (이미 존재하는 코드 가정)
        List<User> findOffice = queryFactory.select(user)
                .from(user)
                .where(user.userLevel.eq(UserLevel.OFFICE))
                .fetch();
        List<Long> officeIds = findOffice.stream()
                .filter(o -> !o.getUsername().equals("office1"))
                .filter(o -> !o.getUsername().equals("office2"))
                .map(User::getUserId)
                .toList();

        // depositData와 calculateData를 officeId 기준으로 조인 혹은 서브쿼리 형태로 합산
        // 여기서는 left join으로 묶고, groupBy로 officeId별로 합산한 예시를 듭니다.

        return queryFactory
                .select(new QCalculateBotRequestDto(
                        Expressions.constant(systemProperty),
                        user.username.as("officeId"), // officeUsername을 officeId로 쓸 경우 수정 필요
                        // totalDepositAmount: 오늘 해당 office에 해당하는 depositBalance 총합
                        queryFactory.select(depositData.depositBalance.sum().coalesce(BigDecimal.ZERO))
                                .from(depositData)
                                .where(depositData.createAt.between(startOfDay, endOfDay)
                                        .and(depositData.officeId.eq(user.userId))
                                        .and(depositData.depositStatus.eq(DepositStatus.COMPLETE))),
                        // totalCalculateAmount: 오늘 해당 office에 해당하는 resultCommission 총합
                        queryFactory.select(depositData.resultCommission.sum().coalesce(BigDecimal.ZERO))
                                .from(depositData)
                                .where(depositData.createAt.between(startOfDay, endOfDay)
                                        .and(depositData.officeId.eq(user.userId))
                                        .and(depositData.depositStatus.eq(DepositStatus.COMPLETE))),
                        // totalWithdrawAmount: 오늘 해당 office에 해당하는 calculateBalance 총합
                        queryFactory.select(calculateData.calculateBalance.sum().coalesce(BigDecimal.ZERO))
                                .from(calculateData)
                                .where(calculateData.createAt.between(startOfDay, endOfDay)
                                        .and(calculateData.officeId.eq(user.userId))
                                        .and(calculateData.calculateStatus.eq(CalculateStatus.COMPLETE))
                                )
                        ,
                        // remainingOfficeAmount: 해당 office의 현재 balance
                        user.balance.coalesce(BigDecimal.ZERO),
                        // status: office의 userStatus
                        user.userStatus.stringValue()
                ))
                .from(user)
                .where(user.userId.in(officeIds))
                .fetch();

    }



    private JPQLQuery<DepositResponseDto> getDepositQuery() {
        return from(depositData)
                .select(
                        Projections.constructor(
                                DepositResponseDto.class,
                                depositData.dataId,
                                depositData.officeId,
                                depositData.officeUsername,
                                depositData.officeNickName,
                                depositData.topAdminId,
                                depositData.topAdminUsername,
                                depositData.topAdminNickName,
                                depositData.basicUserId,
                                depositData.basicUsername,
                                depositData.basicUserNickName,
                                depositData.depositBank,
                                depositData.depositAccount,
                                depositData.officeDepositBank,
                                depositData.officeDepositAccount,
                                depositData.officeDepositName,
                                depositData.txnId,
                                depositData.depositStatus,
                                depositData.depositBalance,
                                depositData.completeAt,
                                depositData.createAt
                        )
                );
    }
}
