package com.solution.calc.domain.money.service;

import com.solution.calc.api.money.dto.*;
import com.solution.calc.api.user.dto.UserResponseDto;
import com.solution.calc.constant.CalculateStatus;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.index.service.IndexService;
import com.solution.calc.domain.money.entity.CalculateData;
import com.solution.calc.domain.money.repository.MoneyRepository;
import com.solution.calc.domain.user.entity.User;
import com.solution.calc.domain.user.service.UserService;
import com.solution.calc.exception.ServiceLogicException;
import com.solution.calc.utils.BotHttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CalculateService {

    private final MoneyRepository moneyRepository;

    private final UserService userService;

    private final IndexService indexService;

    private final BotHttpUtils botHttpUtils;

    // 모든 정상 요청 데이터 조회
    @Transactional(readOnly = true)
    public Page<CalculateResponseDto> findAllCalculate(CalculateSearchRequestDto dto, int page, Long officeId, UserLevel userLevel) {
        return moneyRepository.findAllCalculate(dto, PageRequest.of(page, 10, Sort.by("createAt").descending()), officeId, userLevel);
    }

    // 정산 요청 데이터 생성
    @Transactional
    public CalculateResponseDto createCalculate(CalculatePostRequestDto dto, Long officeId, UserLevel userLevel) {
        // 정산 요청 후 전체 현재 balance 에서 빼기 해야됨
        User office = userService.findUserEntityForUpdate(officeId);
        Optional<CalculateData> calculate = moneyRepository.findCalculate(officeId, CalculateStatus.WAIT);
        if (calculate.isPresent()) {
            throw new ServiceLogicException(ErrorCode.BAD_REQUEST_CALCULATE_EXIST);
        }
        CalculateData calculateData = CalculateData.create(dto, UserResponseDto.of(office));
        BigDecimal calculateBalance = dto.getCalculateBalance();
        if (calculateBalance.compareTo(office.getBalance()) > 0) {
            throw new ServiceLogicException(ErrorCode.BAD_REQUEST);
        }
        office.setBalance(office.getBalance().subtract(calculateBalance));
        userService.delegateSaveUser(office);
        CalculateData saveData = moneyRepository.saveCalculateData(calculateData);
        return CalculateResponseDto.of(saveData);
    }

    // 정산 요청 데이터 수정
    @Transactional
    public CalculateResponseDto updateCalculate(CalculatePatchRequestDto dto, Long officeId, UserLevel userLevel) {
        Long calculateDataId = dto.getCalculateDataId();
        CalculateStatus calculateStatus = dto.getCalculateStatus();
        // 완료 처리시 금일 정산 금액 반영
        // 현재 잔액 반영
        // 취소시 해당 회원 잔액 원복
        CalculateData findData = moneyRepository.findCalculate(calculateDataId);
        BigDecimal calculateBalance = findData.getCalculateBalance();
        CalculateStatus dataStatus = findData.getCalculateStatus();
        if (calculateStatus.equals(CalculateStatus.COMPLETE) && dataStatus.equals(CalculateStatus.WAIT)) {
            Long findOfficeId = findData.getOfficeId();
            UserResponseDto findOffice = userService.findUser(findOfficeId);
            Long adminId = findOffice.getTopAdminId();
            if (adminId == null) {
                adminId = findOfficeId;
            }
            indexService.completeCalculate(calculateBalance, findOfficeId, adminId,userLevel);
            findData.setCalculateStatus(calculateStatus);
        } else if (calculateStatus.equals(CalculateStatus.CANCEL) && dataStatus.equals(CalculateStatus.WAIT)) {
            Long targetOfficeId = findData.getOfficeId();
            User office = userService.findUserEntityForUpdate(targetOfficeId);
            office.setBalance(office.getBalance().add(calculateBalance));
            userService.delegateSaveUser(office);
            findData.setCalculateStatus(calculateStatus);
        } else {
            throw new ServiceLogicException(ErrorCode.BAD_REQUEST);
        }
        return CalculateResponseDto.of(moneyRepository.saveCalculateData(findData));

    }

    @Scheduled(cron = "0 50 * * * *")
    public void runFindAllCalculateEveryHourAt50() {
        List<CalculateBotRequestDto> allCalculate = moneyRepository.findAllCalculate();
        botHttpUtils.sendCalculateAlarm(allCalculate);
    }
}
