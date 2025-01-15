package com.solution.calc.api.user.dto;

import com.solution.calc.constant.UserLevel;
import com.solution.calc.constant.UserStatus;
import com.solution.calc.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Long userId;

    private String username;

    private String nickName;

    private String memo;
    private String rtpayKey;

    private UserStatus userStatus;

    private UserLevel userLevel;

    private String calculateBank;

    private String calculateAccount;

    private String calculateAccountName;

    private String depositBank;

    private String depositAccount;

    private String depositAccountName;

    private double commission;

    private double totalCommission;

    private String telegram;

    private BigDecimal balance;

    private BigDecimal totalDeposit;

    private BigDecimal adminCommissionBalance;

    private BigDecimal agent1CommissionBalance;

    private BigDecimal agent2CommissionBalance;

    private BigDecimal agent3CommissionBalance;


    // agent

    private Long topAdminId;

    private String topAdminUsername;

    private String topAdminNickName;

    private double topAdminCommission;

    private Long agent1Id;

    private String agent1Username;

    private String agent1NickName;

    private double agent1Commission;

    private Long agent2Id;

    private String agent2Username;

    private String agent2NickName;

    private double agent2Commission;

    private Long agent3Id;

    private String agent3Username;

    private String agent3NickName;

    private double agent3Commission;

    private LocalDateTime createAt;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .nickName(user.getNickName())
                .memo(user.getMemo())
                .userStatus(user.getUserStatus())
                .userLevel(user.getUserLevel())
                .calculateBank(user.getCalculateBank())
                .calculateAccount(user.getCalculateAccount())
                .calculateAccountName(user.getCalculateAccountName())
                .depositBank(user.getDepositBank())
                .depositAccount(user.getDepositAccount())
                .depositAccountName(user.getDepositAccountName())
                .commission(user.getCommission())
                .totalCommission(user.getTotalCommission())
                .telegram(user.getTelegram())
                .balance(user.getBalance())

                .totalDeposit(user.getTotalDeposit())
                .adminCommissionBalance(user.getAdminCommissionBalance())
                .agent1CommissionBalance(user.getAgent1CommissionBalance())
                .agent2CommissionBalance(user.getAgent2CommissionBalance())
                .agent3CommissionBalance(user.getAgent3CommissionBalance())

                .topAdminId(user.getTopAdminId())
                .topAdminUsername(user.getTopAdminUsername())
                .topAdminNickName(user.getTopAdminNickName())
                .topAdminCommission(user.getTopAdminCommission())

                .agent1Id(user.getAgent1Id())
                .agent1Username(user.getAgent1Username())
                .agent1NickName(user.getAgent1NickName())
                .agent1Commission(user.getAgent1Commission())

                .agent2Id(user.getAgent2Id())
                .agent2Username(user.getAgent2Username())
                .agent2NickName(user.getAgent2NickName())
                .agent2Commission(user.getAgent2Commission())

                .agent3Id(user.getAgent3Id())
                .agent3Username(user.getAgent3Username())
                .agent3NickName(user.getAgent3NickName())
                .agent3Commission(user.getAgent3Commission())

                .createAt(user.getCreateAt())
                .build();
    }
}
