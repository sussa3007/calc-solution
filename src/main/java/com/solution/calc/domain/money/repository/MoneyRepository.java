package com.solution.calc.domain.money.repository;

import com.solution.calc.api.money.dto.*;
import com.solution.calc.constant.CalculateStatus;
import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.money.entity.CalculateData;
import com.solution.calc.domain.money.entity.DepositData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MoneyRepository {

    DepositData saveDepositData(DepositData data);

    CalculateData saveCalculateData(CalculateData data);

    Page<DepositResponseDto> findAllDeposit(DepositSearchRequestDto dto, Pageable pageable, Long officeId, UserLevel userLevel);

    List<DepositResponseDto> findAllDeposit(DepositExcelSearchRequestDto dto, Long officeId, UserLevel userLevel);

    Page<DepositResponseDto> findAllByBasicUser(String basicUsername, Pageable pageable);

    Page<DepositResponseDto> findLiveDeposit(Pageable pageable, Long officeId, UserLevel userLevel);

    DepositData findDeposit(Long dataId);
    DepositData findDepositByIdForUpdate(Long dataId);

    DepositData findDepositByTx(String txnId);
    Optional<DepositResponseDto> findDepositByUsernameAndStatus(String username);

    CalculateData findCalculate(Long dataId);

    Optional<CalculateData> findCalculate(Long officeId, CalculateStatus status);

    DepositData findDepositByBalanceAndName(String username, BigDecimal balance);

    Page<CalculateResponseDto> findAllCalculate(CalculateSearchRequestDto dto, Pageable pageable, Long officeId, UserLevel userLevel);

    MoneyIndexDto findToday(Long userId, UserLevel userLevel);


}
