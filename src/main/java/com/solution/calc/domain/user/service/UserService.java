package com.solution.calc.domain.user.service;

import com.solution.calc.api.user.dto.*;
import com.solution.calc.auth.jwt.JwtTokenizer;
import com.solution.calc.auth.token.Token;
import com.solution.calc.constant.*;
import com.solution.calc.domain.index.entity.IndexData;
import com.solution.calc.domain.index.service.IndexService;
import com.solution.calc.domain.log.service.LogService;
import com.solution.calc.domain.rtpay.service.RtpayKeyService;
import com.solution.calc.domain.user.entity.BasicUser;
import com.solution.calc.domain.user.entity.User;
import com.solution.calc.domain.user.repository.UserRepository;
import com.solution.calc.exception.ServiceLogicException;
import com.solution.calc.openapi.dto.AutoDepositRequestDto;
import com.solution.calc.openapi.dto.LoginApiResponseDto;
import com.solution.calc.openapi.dto.LoginDto;
import com.solution.calc.openapi.dto.TokenApiResponseDto;
import com.solution.calc.utils.IndexUtils;
import com.solution.calc.utils.WebUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenizer tokenizer;

    private final IndexService indexService;

    private final LogService logService;

    private final RtpayKeyService rtpayKeyService;
    @Value("${PRO_HOST}")
    private String originUrl;


    public LoginApiResponseDto userLogin(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        User findUser = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
        String origin = WebUtils.origin();
        if (username.equals("test")) {
            String memo = findUser.getMemo();
            String ipString = memo.replaceAll(" ", "");
            List<String> ipList = Arrays.stream(ipString.split(",")).toList();
            String currentIp = WebUtils.ip().replaceAll(" ", "");
            if (!ipList.contains(currentIp)) {
                throw new ServiceLogicException(ErrorCode.ACCESS_DENIED, "접근 권한이 없습니다.");
            }
        }
        if (password.equals(findUser.getPassword()) || passwordEncoder.matches(password, findUser.getPassword())) {
            Token token = tokenizer.delegateToken(findUser);
            return LoginApiResponseDto.of(token.getAccessToken(), findUser);
        } else {
            throw new ServiceLogicException(ErrorCode.WRONG_PASSWORD);
        }
    }

    public TokenApiResponseDto apiLogin(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        User findUser = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
        if (password.equals(findUser.getPassword()) || passwordEncoder.matches(password, findUser.getPassword())) {
            Token token = tokenizer.delegateToken(findUser);
            return TokenApiResponseDto.of(token.getAccessToken(), findUser);
        } else {
            throw new ServiceLogicException(ErrorCode.WRONG_PASSWORD);
        }
    }

    public void verifyUser(AutoDepositRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        User findUser = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
        if (!(password.equals(findUser.getPassword()) || passwordEncoder.matches(password, findUser.getPassword()))) {
            throw new ServiceLogicException(ErrorCode.WRONG_PASSWORD);
        }
    }

    public UserResponseDto createUser(UserPostRequestDto dto, Long requestUserId) {
        String password = dto.getPassword();
        String encodePwd = passwordEncoder.encode(password);
        dto.setPassword(encodePwd);
        User createUser = User.create(dto, requestUserId);
        createUser.setRoles(Authority.USER.getStringRole());
        User agent1 = getAgent(createUser.getAgent1Id());
        User agent2 = getAgent(createUser.getAgent2Id());
        User agent3 = getAgent(createUser.getAgent3Id());
        double totalCommission = 0.0;
        totalCommission = dto.getCommission() + totalCommission;
        if (agent1 != null) {
            createUser.setAgent1Username(agent1.getUsername());
            createUser.setAgent1NickName(agent1.getNickName());
            totalCommission = createUser.getAgent1Commission() + totalCommission;
        }
        if (agent2 != null) {
            createUser.setAgent2Username(agent2.getUsername());
            createUser.setAgent2NickName(agent2.getNickName());
            totalCommission = createUser.getAgent2Commission() + totalCommission;
        }
        if (agent3 != null) {
            createUser.setAgent3Username(agent3.getUsername());
            createUser.setAgent3NickName(agent3.getNickName());
            totalCommission = createUser.getAgent3Commission() + totalCommission;
        }
        createUser.setTotalCommission(totalCommission);
        User saveUser = userRepository.saveUser(createUser);
        return UserResponseDto.of(saveUser);
    }

    public User getAgent(Long agentId) {
        if (agentId != null) {
            if (agentId > 0) {
                log.info("agentId = {}", agentId);
                return userRepository.findUserByUserId(agentId)
                        .orElse(null);
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    public UserResponseDto updateUser(UserPatchRequestDto dto, Long userId) {
        User findUser = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
        User updateUser = findUser.updateUser(dto);
        if (dto.getPassword() != null) {
            String encodePwd = passwordEncoder.encode(dto.getPassword());
            updateUser.setPassword(encodePwd);
        }
        User agent1 = getAgent(updateUser.getAgent1Id());
        User agent2 = getAgent(updateUser.getAgent2Id());
        User agent3 = getAgent(updateUser.getAgent3Id());
        double totalCommission = 0.0;
        totalCommission = dto.getCommission() + totalCommission;
        if (agent1 != null) {
            updateUser.setAgent1Username(agent1.getUsername());
            updateUser.setAgent1NickName(agent1.getNickName());
            totalCommission = updateUser.getAgent1Commission() + totalCommission;
        }
        if (agent2 != null) {
            updateUser.setAgent2Username(agent2.getUsername());
            updateUser.setAgent2NickName(agent2.getNickName());
            totalCommission = updateUser.getAgent2Commission() + totalCommission;
        }
        if (agent3 != null) {
            updateUser.setAgent3Username(agent3.getUsername());
            updateUser.setAgent3NickName(agent3.getNickName());
            totalCommission = updateUser.getAgent3Commission() + totalCommission;
        }
        updateUser.setTotalCommission(totalCommission);
        User saveUser = userRepository.saveUser(updateUser);
        String rtpayKey = dto.getRtpayKey();
        String resultKey = "임시 값"+UUID.randomUUID().toString().substring(0, 10);
        if (rtpayKey != null && !rtpayKey.isBlank()) {
            resultKey = rtpayKey.replaceAll(" ", "");
        }
        rtpayKeyService.createAndUpdateRtpayKey(saveUser.getUsername(), resultKey);
        return UserResponseDto.of(saveUser);
    }

    public BasicUserResponseDto updateBasicUser(BasicUserPatchRequestDto dto, Long basicUserId) {
        BasicUser findUser = userRepository.findBasicUserById(basicUserId)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
        BasicUser updateBasicUser = findUser.update(dto);
        BasicUser basicUser = userRepository.saveBasicUser(updateBasicUser);
        return BasicUserResponseDto.of(basicUser);
    }

    public BasicUserResponseDto updateBasicUserTotalBalance(Long basicUserId, BigDecimal balance) {
        BasicUser findUser = userRepository.findBasicUserById(basicUserId)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
        findUser.setTotalDepositBalance(findUser.getTotalDepositBalance().add(balance));
        return BasicUserResponseDto.of(userRepository.saveBasicUser(findUser));
    }

    public BasicUserResponseDto updateBasicUserSubTotalBalance(Long basicUserId, BigDecimal balance) {
        BasicUser findUser = userRepository.findBasicUserById(basicUserId)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
        findUser.setTotalDepositBalance(findUser.getTotalDepositBalance().subtract(balance));
        return BasicUserResponseDto.of(userRepository.saveBasicUser(findUser));
    }

    public boolean updateUserBalance(BalanceRequestDto dto) {
        Long userId = dto.getUserId();
        BigDecimal requestBalance = dto.getRequestBalance();
        BalanceStatus requestType = dto.getRequestType();
        User findUser = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
        // TODO 지급, 회수 할경우 계좌 잔액과 어떻게 비교할건지? -> 지급,회수 경우 관리자 잔액에 반영되어야 계좌 잔액과 일치 할수 있음
        BigDecimal userBalance = findUser.getBalance();
        if (requestType.equals(BalanceStatus.PAYMENT)) {
            BigDecimal targetBalance = userBalance.add(requestBalance);
            findUser.setBalance(targetBalance);
            logService.saveMoneyLog(
                    "admin",
                    findUser.getUsername(),
                    LogType.USER_MONEY_PAYMENT,
                    requestBalance.longValue(),
                    userBalance.longValue(),
                    targetBalance.longValue()
            );
        } else {
            BigDecimal targetBalance = userBalance.subtract(requestBalance);
            if (targetBalance.signum() < 0) {
                throw new ServiceLogicException(ErrorCode.BAD_REQUEST_MONEY_COLLECT);
            }
            findUser.setBalance(targetBalance);
            logService.saveMoneyLog(
                    "admin",
                    findUser.getUsername(),
                    LogType.USER_MONEY_COLLECT,
                    requestBalance.longValue(),
                    userBalance.longValue(),
                    targetBalance.longValue()
            );
        }
        User saveUser = userRepository.saveUser(findUser);

        return true;
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUser(Long userId) {
        User findUser = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
        UserResponseDto userResponseDto = UserResponseDto.of(findUser);
        String rtpayKey = rtpayKeyService.getRtpayKey(userResponseDto.getUsername());
        userResponseDto.setRtpayKey(rtpayKey);
        return userResponseDto;
    }

    public User findUserEntity(Long userId) {
        if (userId != null) {
            return userRepository.findUserByUserId(userId)
                    .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
        } else {
            return null;
        }
    }

    public User findUserEntity(String username) {
        if (username != null) {
            return userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
        } else {
            return null;
        }
    }

    public User findUserEntityForUpdate(Long userId) {
        if (userId != null) {
            return userRepository.findUserByUserIdForUpdate(userId)
                    .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
        } else {
            return null;
        }
    }

    public boolean delegateSaveUser(User user) {
        userRepository.saveUser(user);
        return true;
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAllUser(int page, User user) {
        UserLevel userLevel = user.getUserLevel();
        Long userId = user.getUserId();
        return userRepository.findAllAdmin(PageRequest.of(page, 500), userLevel, userId)
                .map(UserResponseDto::of);
    }

    @Transactional(readOnly = true)
    public Page<BasicUserResponseDto> findBasicUser(BasicUserSearchDto dto, int page, Long officeUserId) {
        return userRepository.findAllBasicUserByOffice(dto, officeUserId, PageRequest.of(page, 10, Sort.by("createAt").descending()));
    }

    @Transactional(readOnly = true)
    public Page<BasicUserResponseDto> findAllBasicUser(BasicUserSearchDto dto, int page) {
        return userRepository.findAllBasicUser(dto, PageRequest.of(page, 10, Sort.by("createAt").descending()));
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAllAgent(int page) {
        return userRepository.findAllAgent(PageRequest.of(page, 500))
                .map(UserResponseDto::of);
    }


    public void verifyUserStatus(UserStatus userStatus) {
        if (userStatus.equals(UserStatus.BLOCK) || userStatus.equals(UserStatus.INACTIVE)) {
            throw new ServiceLogicException(ErrorCode.BLOCK_OR_INACTIVE_USER);
        }
    }

    public BasicUser findAndCreateBasicUser(String username, String nickName, String depositBank, String depositAccount, Long officeId) {
        Optional<BasicUser> basicUserParam = userRepository.findBasicUserParam(username, officeId);
        if (basicUserParam.isPresent()) {
            BasicUser basicUser = basicUserParam.get();
            verifyUserStatus(basicUser.getUserStatus());
            if (!basicUser.getBank().equals(depositBank) || !basicUser.getAccount().equals(depositAccount)) {
                basicUser.setBank(depositBank);
                basicUser.setAccount(depositAccount);
                userRepository.saveBasicUser(basicUser);
            }
            return basicUser;
        } else {
            BasicUser bu = BasicUser.create(username, nickName, depositBank, depositAccount, officeId);
            User officeUser = userRepository.findUserByUserId(bu.getOfficeId())
                    .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
            bu.setOfficeUsername(officeUser.getUsername());
            bu.setOfficeNickName(officeUser.getNickName());
//            bu.setUsername(UUID.randomUUID() + "-" + officeUser.getUsername());
            return userRepository.saveBasicUser(bu);
        }
    }

    @Transactional(readOnly = true)
    public Page<AgentUserResponseDto> findAgentUserInfo(Long agentId, int page) {
        Page<UserResponseDto> agentOffice = userRepository.findAgentOffice(agentId, PageRequest.of(page, 10));
        return agentOffice.map(ao -> {
            double agentCommission = getAgentCommission(agentId, ao) / 100;
            BigDecimal agentCommissionBalance = getAgentCommissionBalance(agentId, ao);
            IndexData indexData = indexService.verifyIndexDataForGet(IndexUtils.getDateKey(LocalDate.now(), ao.getUserId()), ao.getUserId());
            BigDecimal todayDepositBalance = indexData.getTodayDepositBalance();
            return AgentUserResponseDto.builder()
                    .officeUsername(ao.getUsername())
                    .officeNickName(ao.getNickName())
                    .totalDeposit(ao.getTotalDeposit())
                    .agentCommission(agentCommissionBalance)
                    .todayOfficeDeposit(todayDepositBalance)
                    .todayAgentCommission(todayDepositBalance.multiply(BigDecimal.valueOf(agentCommission)))
                    .currentTime(LocalDate.now())
                    .build();
        });
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

    public BigDecimal getAgentCommissionBalance(Long agentId, UserResponseDto dto) {
        Long agent1Id = dto.getAgent1Id();
        Long agent2Id = dto.getAgent2Id();
        Long agent3Id = dto.getAgent3Id();
        if (agentId.equals(agent1Id)) {
            return dto.getAgent1CommissionBalance();
        } else if (agentId.equals(agent2Id)) {
            return dto.getAgent2CommissionBalance();
        } else if (agentId.equals(agent3Id)) {
            return dto.getAgent3CommissionBalance();
        }
        return BigDecimal.valueOf(0);

    }
}
