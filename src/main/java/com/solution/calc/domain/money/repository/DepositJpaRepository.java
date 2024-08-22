package com.solution.calc.domain.money.repository;

import com.solution.calc.constant.DepositStatus;
import com.solution.calc.domain.money.entity.DepositData;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.math.BigDecimal;
import java.util.Optional;

public interface DepositJpaRepository extends JpaRepository<DepositData, Long> {

    Optional<DepositData> findByTxnId(String txnId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DepositData> findByDataId(Long dataId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DepositData> findByBasicUserNickNameAndDepositBalanceAndDepositStatus(String basicUserNickName, BigDecimal depositBalance, DepositStatus status);
}
