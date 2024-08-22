package com.solution.calc.utils;

import com.solution.calc.api.index.dto.BasicUserInfo;
import com.solution.calc.api.index.dto.IndexResponseDto;
import com.solution.calc.api.money.dto.CalculateResponseDto;
import com.solution.calc.api.money.dto.DepositResponseDto;
import com.solution.calc.api.user.dto.BasicUserResponseDto;
import com.solution.calc.api.user.dto.UserResponseDto;
import com.solution.calc.constant.CalculateStatus;
import com.solution.calc.constant.DepositStatus;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.domain.user.repository.UserRepository;
import com.solution.calc.exception.ServiceLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class StubUtils {

    private final UserRepository userRepository;

    public Page<UserResponseDto> getAdmin() {
        return userRepository.findAllAdmin(PageRequest.of(0, 10))
                .map(UserResponseDto::of);
    }

    public Page<UserResponseDto> getAgent() {
        return userRepository.findAllAgent(PageRequest.of(0, 10))
                .map(UserResponseDto::of);
    }


    public UserResponseDto getUser(Long userId) {
        return UserResponseDto.of(
                userRepository.findUserByUserId(userId)
                        .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER))
        );
    }

    public BasicUserResponseDto getBasicUser(Long userId) {
        return BasicUserResponseDto.of(
                userRepository.findBasicUserById(userId)
                        .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER))
        );
    }

    public IndexResponseDto getIndex() {
        BasicUserInfo build = BasicUserInfo.builder()
                .username("aaa1234")
                .nickName("테스트")
                .balance(BigDecimal.valueOf(549283742))
                .build();
        return IndexResponseDto.builder()
                .dataId(1L)
                .date(LocalDate.now())
                .todayDepositBalance(BigDecimal.valueOf(1978840000))
                .todayCalculateBalance(BigDecimal.valueOf(1952000000))
                .yesterdayDepositBalance(BigDecimal.valueOf(2867020000L))
                .yesterdayCalculateBalance(BigDecimal.valueOf(2832615760L))
                .todayCalculateCommission(BigDecimal.valueOf(4353448))
                .yesterdayCalculateCommission(BigDecimal.valueOf(6307444))
                .todayTotalBalance(BigDecimal.valueOf(22486552))
                .yesterdayTotalBalance(BigDecimal.valueOf(21906528))
                .monthTotalBalance(BigDecimal.valueOf(42098250000L))
                .topBasicUser(List.of(build,build,build,build,build))
                .createAt(LocalDateTime.now())
                .build();
    }

    public Page<CalculateResponseDto> getCalculatePage() {
        return new PageImpl(
                List.of(getCalculate(CalculateStatus.WAIT),getCalculate(CalculateStatus.COMPLETE),getCalculate(CalculateStatus.COMPLETE))
                ,PageRequest.of(0,10),3);
    }

    public CalculateResponseDto getCalculate(CalculateStatus status) {
        return CalculateResponseDto.builder()
                .dataId(1L)
                .officeId(2L)
                .officeUsername("Office")
                .officeNickName("Office")
                .topAdminId(1L)
                .topAdminUsername("Admin")
                .topAdminNickName("Admin")
                .calculateStatus(status)
                .calculateBalance(BigDecimal.valueOf(1000000))
                .completeAt(LocalDateTime.now())
                .createAt(LocalDateTime.now())
                .build();
    }
    public Page<DepositResponseDto> getDepositPage() {
        return new PageImpl(
                List.of(getDepositData(DepositStatus.WAIT),getDepositData(DepositStatus.DEPOSIT_COMPLETE),getDepositData(DepositStatus.COMPLETE),getDepositData(DepositStatus.COMPLETE))
                ,PageRequest.of(0,10),3);
    }

    public Page<DepositResponseDto> getDepositPageLive() {
        return new PageImpl(
                List.of(getDepositData(DepositStatus.DEPOSIT_COMPLETE),getDepositData(DepositStatus.DEPOSIT_COMPLETE),getDepositData(DepositStatus.WAIT),getDepositData(DepositStatus.WAIT))
                ,PageRequest.of(0,10),3);
    }

    public DepositResponseDto getDepositData(DepositStatus status) {
        return DepositResponseDto.builder()
                .dataId(1L)
                .officeId(2L)
                .officeUsername("Office")
                .officeNickName("Office")
                .topAdminId(1L)
                .topAdminUsername("Admin")
                .topAdminNickName("Admin")
                .basicUserId(1L)
                .basicUsername("QDGFD-Office")
                .basicUserNickName("홍길동")
                .depositBank("카카오뱅크")
                .depositAccount("333312341234")
                .txnId("TDKG-DFGE-DFQEF-dFSDF-ASDF1")
                .depositStatus(status)
                .depositBalance(BigDecimal.valueOf(20000000))
                .completeAt(LocalDateTime.now())
                .createAt(LocalDateTime.now())
                .build();
    }




}
