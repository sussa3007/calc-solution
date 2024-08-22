package com.solution.calc.domain.index.service;

import com.solution.calc.api.index.dto.IndexResponseDto;
import com.solution.calc.api.index.dto.IndexSearchDto;
import com.solution.calc.api.money.dto.MoneyIndexDto;
import com.solution.calc.api.user.dto.AgentCalculateResponseDto;
import com.solution.calc.api.user.dto.AgentUserResponseDto;
import com.solution.calc.api.user.dto.UserResponseDto;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.index.entity.IndexData;
import com.solution.calc.domain.index.repository.IndexDataJpaRepository;
import com.solution.calc.domain.money.repository.MoneyRepository;
import com.solution.calc.domain.user.repository.UserRepository;
import com.solution.calc.exception.ServiceLogicException;
import com.solution.calc.utils.IndexUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndexService {

    private final IndexDataJpaRepository indexDataJpaRepository;

    private final UserRepository userRepository;

    private final MoneyRepository moneyRepository;

    @Transactional
    public IndexResponseDto findIndex(Long userId, UserLevel userLevel) {
        IndexData indexData = verifyIndexDataForGet(IndexUtils.getDateKey(LocalDate.now(), userId), userId, userLevel);
        MoneyIndexDto today = moneyRepository.findToday(userId, userLevel);
        if (today == null) {
            return IndexResponseDto.of(indexData);
        } else {
            return IndexResponseDto.of(today, userLevel);
        }
    }

    @Transactional(readOnly = true)
    public Page<IndexResponseDto> findIndexList(Long userId, IndexSearchDto indexSearchDto) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (indexSearchDto.getStartDate() != null && indexSearchDto.getEndDate() != null) {
            start = indexSearchDto.getStartDate().atStartOfDay();
            end = indexSearchDto.getEndDate().atTime(LocalTime.MAX);
            return indexDataJpaRepository.findByUserIdAndCreateAtBetween(userId, start, end, PageRequest.of(indexSearchDto.getPage(), 1000, Sort.by("createAt").descending()))
                    .map(IndexResponseDto::of);
        } else {
            return indexDataJpaRepository.findByMonthAndUserId(LocalDate.now().getMonthValue(), userId, PageRequest.of(indexSearchDto.getPage(), 1000, Sort.by("createAt").descending()))
                    .map(IndexResponseDto::of);

        }
    }

    @Transactional(readOnly = true)
    public Page<AgentCalculateResponseDto> findAgentIndexList(Long userId, IndexSearchDto indexSearchDto) {

        Page<UserResponseDto> agentOffice = userRepository.findAgentOffice(userId, PageRequest.of(indexSearchDto.getPage(), 10));
        return agentOffice.map(ao -> {
            double agentCommission = getAgentCommission(userId, ao) / 100;
            Page<IndexResponseDto> indexData;
            if (indexSearchDto.getStartDate() != null && indexSearchDto.getEndDate() != null) {
                LocalDateTime start = indexSearchDto.getStartDate().atStartOfDay();
                LocalDateTime end = indexSearchDto.getEndDate().atTime(LocalTime.MAX);
                indexData = indexDataJpaRepository.findByUserIdAndCreateAtBetween(ao.getUserId(), start, end, PageRequest.of(indexSearchDto.getPage(), 1000, Sort.by("createAt").descending()))
                        .map(IndexResponseDto::of);
            } else {
                indexData = indexDataJpaRepository.findByMonthAndUserId(LocalDate.now().getMonthValue(), ao.getUserId(), PageRequest.of(indexSearchDto.getPage(), 1000, Sort.by("createAt").descending()))
                        .map(IndexResponseDto::of);

            }

            List<AgentUserResponseDto> content = indexData.map(id -> {
                BigDecimal todayDepositBalance = id.getTodayDepositBalance();
                return AgentUserResponseDto.builder()
                        .createAt(id.getCreateAt())
                        .officeUsername(ao.getUsername())
                        .officeNickName(ao.getNickName())
                        .totalDeposit(ao.getTotalDeposit())
                        .agentCommission(todayDepositBalance.multiply(BigDecimal.valueOf(agentCommission)))
                        .todayOfficeDeposit(todayDepositBalance)
                        .todayAgentCommission(todayDepositBalance.multiply(BigDecimal.valueOf(agentCommission)))
                        .currentTime(LocalDate.now())
                        .build();
            }).getContent();
            return AgentCalculateResponseDto.builder()
                    .officeId(ao.getUserId())
                    .officeUsername(ao.getUsername())
                    .officeNickname(ao.getNickName())
                    .calculateData(content)
                    .build();
        });

    }

    @Transactional
    public boolean completeDeposit(BigDecimal depositBalance, Long officeId, Long adminId, UserLevel userLevel) {
        setTodayDeposit(depositBalance, adminId, userLevel);
        if (!officeId.equals(adminId)) {
            setTodayDeposit(depositBalance, officeId, UserLevel.OFFICE);
        }
        return true;
    }

    @Transactional
    public boolean cancelDeposit(BigDecimal depositBalance, Long officeId, Long adminId, UserLevel userLevel) {
        setSubTodayDeposit(depositBalance, adminId, userLevel);
        if (!officeId.equals(adminId)) {
            setSubTodayDeposit(depositBalance, officeId, UserLevel.OFFICE);
        }
        return true;
    }

    @Transactional
    public boolean completeCalculate(BigDecimal calculateBalance, Long officeId, Long adminId, UserLevel userLevel) {
        setTodayCalculate(calculateBalance, adminId, userLevel);
        if (!officeId.equals(adminId)) {
            setTodayCalculate(calculateBalance, officeId, UserLevel.OFFICE);
        }
        return true;
    }

    private void setTodayDeposit(BigDecimal depositBalance, Long userId, UserLevel userLevel) {
        IndexData indexData = verifyIndexDataForUpdate(IndexUtils.getDateKey(LocalDate.now(), userId), userId);
        indexData.setTodayDepositBalance(indexData.getTodayDepositBalance().add(depositBalance));
        indexData.setTodayTotalBalance(indexData.getTodayTotalBalance().add(depositBalance));
//        indexData.setResultCurrentBalance(indexData.getResultCurrentBalance().add(depositBalance));
        indexDataJpaRepository.save(indexData);
    }

    private void setSubTodayDeposit(BigDecimal depositBalance, Long userId, UserLevel userLevel) {
        IndexData indexData = verifyIndexDataForUpdate(IndexUtils.getDateKey(LocalDate.now(), userId), userId);
        indexData.setTodayDepositBalance(indexData.getTodayDepositBalance().subtract(depositBalance));
        indexData.setTodayTotalBalance(indexData.getTodayTotalBalance().subtract(depositBalance));
//        indexData.setResultCurrentBalance(indexData.getResultCurrentBalance().subtract(depositBalance));
        indexDataJpaRepository.save(indexData);
    }

    private void setTodayCalculate(BigDecimal calculateBalance, Long userId, UserLevel userLevel) {
        IndexData indexData = verifyIndexDataForUpdate(IndexUtils.getDateKey(LocalDate.now(), userId), userId);
        if (indexData != null) {
            indexData.setTodayCalculateBalance(indexData.getTodayCalculateBalance().add(calculateBalance));
            indexData.setTodayTotalBalance(indexData.getTodayTotalBalance().subtract(calculateBalance));
//        indexData.setResultCurrentBalance(indexData.getResultCurrentBalance().subtract(calculateBalance));
            indexDataJpaRepository.save(indexData);
        }

    }

    @Transactional
    public boolean completeCommission(BigDecimal commission, Long officeId, Long adminId, UserLevel userLevel) {
        IndexData adminData = verifyIndexDataForUpdate(IndexUtils.getDateKey(LocalDate.now(), adminId), adminId);
        adminData.setTodayCalculateCommission(adminData.getTodayCalculateCommission().add(commission));
        indexDataJpaRepository.save(adminData);
        if (!officeId.equals(adminId)) {
            IndexData officeData = verifyIndexDataForUpdate(IndexUtils.getDateKey(LocalDate.now(), officeId), officeId);
            officeData.setTodayCalculateCommission(officeData.getTodayCalculateCommission().add(commission));
            indexDataJpaRepository.save(officeData);
        }
        return true;
    }

    @Transactional
    public boolean cancelCommission(BigDecimal commission, Long officeId, Long adminId, UserLevel userLevel) {
        IndexData adminData = verifyIndexDataForUpdate(IndexUtils.getDateKey(LocalDate.now(), adminId), adminId);
        adminData.setTodayCalculateCommission(adminData.getTodayCalculateCommission().subtract(commission));
        indexDataJpaRepository.save(adminData);
        if (!officeId.equals(adminId)) {
            IndexData officeData = verifyIndexDataForUpdate(IndexUtils.getDateKey(LocalDate.now(), officeId), officeId);
            officeData.setTodayCalculateCommission(officeData.getTodayCalculateCommission().subtract(commission));
            indexDataJpaRepository.save(officeData);
        }
        return true;
    }

    @Transactional
    public IndexData verifyIndexDataForGet(String dateKey, Long userId, UserLevel userLevel) {
        Optional<IndexData> findOpData = indexDataJpaRepository.findByDateKeyAndUserLevel(dateKey,userLevel);
        return getIndexData(dateKey, userId, findOpData);
    }


    @Transactional
    public IndexData verifyIndexDataForUpdate(String dateKey, Long userId) {
        Optional<IndexData> findOpData = indexDataJpaRepository.findByDateKey(dateKey);
        return getIndexData(dateKey, userId, findOpData);
    }



    @Transactional
    public IndexData verifyIndexDataForGet(String dateKey, Long userId) {
        Optional<IndexData> findOpData = indexDataJpaRepository.findByDateKeyAndUserId(dateKey, userId);
        return getIndexData(dateKey, userId, findOpData);
    }

    private IndexData getIndexData(String dateKey, Long userId, Optional<IndexData> findOpData) {
        if (findOpData.isPresent()) {
            return findOpData.get();
        } else {
            UserResponseDto user = UserResponseDto.of(userRepository.findUserByUserId(userId)
                    .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER)));
            if (user.getUserLevel().equals(UserLevel.AGENT)) {
                return null;
            }
            LocalDate yesterday = LocalDate.now().minusDays(1L);
            String yesterdayDateKey = IndexUtils.getDateKey(yesterday, userId);
            IndexData findOpYesterdayData = indexDataJpaRepository.findByDateKey(yesterdayDateKey)
                    .orElse(null);
            IndexData newData = IndexData.create(dateKey, findOpYesterdayData, userId, user);
            return indexDataJpaRepository.save(newData);
        }
    }

    public double getAgentCommission(Long agentId, UserResponseDto dto) {
        Long agent1Id = dto.getAgent1Id();
        Long agent2Id = dto.getAgent2Id();
        Long agent3Id = dto.getAgent3Id();
        if (agentId.equals(agent1Id)) {
            return dto.getAgent1Commission();
        } else if (agentId.equals(agent2Id)) {
            return dto.getAgent2Commission();
        } else if (agentId.equals(agent3Id)) {
            return dto.getAgent3Commission();
        }
        return 0.0;

    }


}
