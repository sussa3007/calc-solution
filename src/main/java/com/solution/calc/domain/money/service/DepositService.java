package com.solution.calc.domain.money.service;

import com.solution.calc.api.money.dto.*;
import com.solution.calc.api.user.dto.UserResponseDto;
import com.solution.calc.constant.*;
import com.solution.calc.domain.index.service.IndexService;
import com.solution.calc.domain.log.service.LogService;
import com.solution.calc.domain.money.entity.DepositData;
import com.solution.calc.domain.money.repository.MoneyRepository;
import com.solution.calc.domain.user.entity.BasicUser;
import com.solution.calc.domain.user.entity.User;
import com.solution.calc.domain.user.service.UserService;
import com.solution.calc.exception.ServiceLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepositService {

    private final MoneyRepository moneyRepository;

    private final UserService userService;

    private final IndexService indexService;

    private final LogService logService;

    @Transactional(readOnly = true)
    public Page<DepositResponseDto> findAllDeposit(DepositSearchRequestDto dto, int page, Long officeId, UserLevel userLevel) {
        return moneyRepository.findAllDeposit(dto, PageRequest.of(page, 10, Sort.by("createAt").descending()), officeId, userLevel);
    }

    @Transactional(readOnly = true)
    public List<DepositExcelDto> findAllDeposit(DepositExcelSearchRequestDto dto, Long officeId, UserLevel userLevel) {
        return moneyRepository.findAllDeposit(dto, officeId, userLevel).stream().map(DepositExcelDto::of).toList();
    }

    @Transactional(readOnly = true)
    public Page<DepositResponseDto> findLiveDeposit(int page, Long officeId, UserLevel userLevel) {
        return moneyRepository.findLiveDeposit(PageRequest.of(page, 500, Sort.by("createAt").ascending()), officeId, userLevel);

    }

    @Transactional(readOnly = true)
    public DepositResponseDto findDeposit(Long dataId) {
        return DepositResponseDto.of(moneyRepository.findDeposit(dataId));
    }

    @Transactional(readOnly = true)
    public DepositResponseDto findDepositByTx(String txnId) {
        return DepositResponseDto.of(moneyRepository.findDepositByTx(txnId));
    }


    @Transactional(readOnly = true)
    public Page<DepositResponseDto> findBasicUserDeposit(int page, String basicUsername) {
        if (basicUsername == null || basicUsername.isEmpty()) throw new ServiceLogicException(ErrorCode.BAD_REQUEST);
        return moneyRepository.findAllByBasicUser(basicUsername, PageRequest.of(page, 15, Sort.by("createAt").descending()));
    }

    @Transactional
    public DepositResponseDto setDepositComplete(Map data) {
        String rname = (String) data.get("RNAME");
        String rpay = (String) data.get("RPAY");
        long balance = Long.parseLong(rpay);
        DepositData findDeposit = moneyRepository.findDepositByBalanceAndName(rname, BigDecimal.valueOf(balance));
        if (findDeposit.getDepositStatus().equals(DepositStatus.WAIT)) {
            findDeposit.setDepositStatus(DepositStatus.DEPOSIT_COMPLETE);
        }
        DepositData saveDepositData = moneyRepository.saveDepositData(findDeposit);
        return DepositResponseDto.of(saveDepositData);
    }

    @Transactional
    public DepositResponseDto createDeposit(DepositPostRequestDto dto, Long officeId, UserLevel userLevel) {
        /*
         * 기업 회원의 하위 일반 회원에 이름, 계좌 중복 있으면 안됨
         * */
        long balance = dto.getDepositBalance().longValue();
        String stringBalance = String.valueOf(balance);
        String sub = stringBalance.substring(stringBalance.length() - 4);

        if (balance < 10000 || !sub.equals("0000")) {
            throw new ServiceLogicException(ErrorCode.BAD_REQUEST_DEPOSIT_BALANCE);
        }
        String nickName = dto.getDepositName();
        String depositBank = dto.getDepositBank();
        String depositAccount = dto.getDepositAccount();
        String depositUsername = dto.getDepositUsername();
        if (moneyRepository.findDepositByUsernameAndStatus(depositUsername).isPresent()) throw new ServiceLogicException(ErrorCode.BAD_REQUEST_DEPOSIT_EXIST);
        UserResponseDto office = userService.findUser(officeId);
        if (office.getUserStatus().equals(UserStatus.BLOCK) || office.getUserStatus().equals(UserStatus.INACTIVE)) {
            throw new ServiceLogicException(ErrorCode.BAD_REQUEST_DEPOSIT_INACTIVE);
        }
        BasicUser basicUser = userService.findAndCreateBasicUser(depositUsername, nickName, depositBank, depositAccount, officeId);
        DepositData newDepositData = DepositData.create(dto, basicUser, office);
        DepositData depositData = moneyRepository.saveDepositData(newDepositData);
        return DepositResponseDto.of(depositData);
    }

    @Transactional
    public DepositResponseDto patchDeposit(DepositPatchRequestDto dto, Long officeId, UserLevel userLevel) {
        // 입금 완료 시 취소 불가
        // 처리 완료 시 데시보드 데이터 반영
        // 처리 완료 시 요율 반영
        // 처리시 읿반 회원 데이터 토탈 입금 액 반영
        Long depositDataId = dto.getDepositDataId();
        DepositStatus depositStatus = dto.getDepositStatus();
        DepositData findDeposit = moneyRepository.findDepositByIdForUpdate(depositDataId);
        DepositStatus currentStatus = findDeposit.getDepositStatus();

        BigDecimal depositBalance = findDeposit.getDepositBalance();
        Long adminId = findDeposit.getTopAdminId();
        Long officeTrueId = findDeposit.getOfficeId();

        User findOffice = userService.findUserEntityForUpdate(officeTrueId);

        Long agent1Id = findOffice.getAgent1Id();
        Long agent2Id = findOffice.getAgent2Id();
        Long agent3Id = findOffice.getAgent3Id();
        double adminCommission = findOffice.getCommission()/100;
        double agent1Commission = findOffice.getAgent1Commission()/100;
        double agent2Commission = findOffice.getAgent2Commission()/100;
        double agent3Commission = findOffice.getAgent3Commission()/100;
        BigDecimal adminCommissionBalance = depositBalance.multiply(BigDecimal.valueOf(adminCommission));
        BigDecimal agent1CommissionBalance = depositBalance.multiply(BigDecimal.valueOf(agent1Commission));
        BigDecimal agent2CommissionBalance = depositBalance.multiply(BigDecimal.valueOf(agent2Commission));
        BigDecimal agent3CommissionBalance = depositBalance.multiply(BigDecimal.valueOf(agent3Commission));

        BigDecimal totalCommission = adminCommissionBalance.add(agent1CommissionBalance).add(agent2CommissionBalance).add(agent3CommissionBalance);
        BigDecimal resultBalance = depositBalance.subtract(totalCommission);
        if (depositStatus.equals(DepositStatus.COMPLETE) && currentStatus.equals(DepositStatus.COMPLETE)) {
            throw new ServiceLogicException(ErrorCode.BAD_REQUEST_DEPOSIT_COMPLETE);
        }

        if (currentStatus.equals(DepositStatus.COMPLETE) && depositStatus.equals(DepositStatus.CANCEL)) {
            BigDecimal officeResultBalance = findOffice.getBalance().subtract(resultBalance);
            if (officeResultBalance.signum() < 0) {
                throw new ServiceLogicException(ErrorCode.BAD_REQUEST_DEPOSIT_CANCEL);
            }
            setSubUserCommission(adminId, adminCommissionBalance);
            setSubUserCommission(agent1Id, agent1CommissionBalance);
            setSubUserCommission(agent2Id, agent2CommissionBalance);
            setSubUserCommission(agent3Id, agent3CommissionBalance);
            findDeposit.setResultCommission(BigDecimal.valueOf(0));
            findDeposit.setResultBalance(BigDecimal.valueOf(0));

            findOffice.setBalance(officeResultBalance);
            findOffice.setTotalDeposit(findOffice.getTotalDeposit().subtract(depositBalance));
            findOffice.setAdminCommissionBalance(findOffice.getAdminCommissionBalance().subtract(adminCommissionBalance));
            findOffice.setAgent1CommissionBalance(findOffice.getAgent1CommissionBalance().subtract(agent1CommissionBalance));
            findOffice.setAgent2CommissionBalance(findOffice.getAgent2CommissionBalance().subtract(agent2CommissionBalance));
            findOffice.setAgent3CommissionBalance(findOffice.getAgent3CommissionBalance().subtract(agent3CommissionBalance));
            userService.delegateSaveUser(findOffice);
            indexService.cancelDeposit(depositBalance, officeTrueId, adminId,userLevel);
            indexService.cancelCommission(totalCommission, officeTrueId, adminId,userLevel);
            logService.saveCancelLog("admin", LogType.DEPOSIT_COMPLETE_CANCEL, findDeposit.getBasicUsername(), findDeposit.getTxnId());
            userService.updateBasicUserSubTotalBalance(findDeposit.getBasicUserId(), findDeposit.getDepositBalance());
        } else if (depositStatus.equals(DepositStatus.COMPLETE)) {
            setAddUserCommission(adminId, adminCommissionBalance);
            setAddUserCommission(agent1Id, agent1CommissionBalance);
            setAddUserCommission(agent2Id, agent2CommissionBalance);
            setAddUserCommission(agent3Id, agent3CommissionBalance);
            findDeposit.setResultBalance(resultBalance);
            findDeposit.setResultCommission(totalCommission);

            findOffice.setBalance(findOffice.getBalance().add(resultBalance));
            findOffice.setTotalDeposit(findOffice.getTotalDeposit().add(depositBalance));
            findOffice.setAdminCommissionBalance(findOffice.getAdminCommissionBalance().add(adminCommissionBalance));
            findOffice.setAgent1CommissionBalance(findOffice.getAgent1CommissionBalance().add(agent1CommissionBalance));
            findOffice.setAgent2CommissionBalance(findOffice.getAgent2CommissionBalance().add(agent2CommissionBalance));
            findOffice.setAgent3CommissionBalance(findOffice.getAgent3CommissionBalance().add(agent3CommissionBalance));
            userService.delegateSaveUser(findOffice);
            indexService.completeDeposit(depositBalance, officeTrueId, adminId,userLevel);
            indexService.completeCommission(totalCommission, officeTrueId, adminId,userLevel);
            userService.updateBasicUserTotalBalance(findDeposit.getBasicUserId(), findDeposit.getDepositBalance());
        }
        findDeposit.setCompleteAt(LocalDateTime.now());
        findDeposit.setDepositStatus(depositStatus);

        return DepositResponseDto.of(moneyRepository.saveDepositData(findDeposit));
    }

    private void setAddUserCommission(Long userId, BigDecimal commissionBalance) {
        if (userId != null && userId != 0) {
            User agent = userService.findUserEntityForUpdate(userId);
            if (agent != null) {
                agent.setBalance(agent.getBalance().add(commissionBalance));
                userService.delegateSaveUser(agent);
            }
        }

    }

    private void setSubUserCommission(Long userId, BigDecimal commissionBalance) {
        if (userId != null && userId != 0) {
            User agent = userService.findUserEntityForUpdate(userId);
            if (agent != null) {
                agent.setBalance(agent.getBalance().subtract(commissionBalance));
                userService.delegateSaveUser(agent);
            }
        }

    }

}
