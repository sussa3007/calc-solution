package com.solution.calc.domain.user.entity;

import com.solution.calc.api.user.dto.UserPatchRequestDto;
import com.solution.calc.api.user.dto.UserPostRequestDto;
import com.solution.calc.audit.Auditable;
import com.solution.calc.constant.UserLevel;
import com.solution.calc.constant.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String memo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel;

    @Column(nullable = false)
    private String calculateBank;

    @Column(nullable = false)
    private String calculateAccount;

    @Column(nullable = false)
    private String calculateAccountName;

    @Column(nullable = false)
    private String depositBank;

    @Column(nullable = false)
    private String depositAccount;

    @Column(nullable = false)
    private String depositAccountName;

    @Column(nullable = false)
    private double commission;

    @Column(nullable = false)
    private double totalCommission;

    @Column(nullable = false)
    private String telegram;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private BigDecimal totalDeposit;

    @Column(nullable = false)
    private BigDecimal adminCommissionBalance;

    @Column(nullable = false)
    private BigDecimal agent1CommissionBalance;

    @Column(nullable = false)
    private BigDecimal agent2CommissionBalance;

    @Column(nullable = false)
    private BigDecimal agent3CommissionBalance;


    // agent

    @Column(nullable = true)
    private Long topAdminId;

    @Column(nullable = true)
    private String topAdminUsername;

    @Column(nullable = true)
    private String topAdminNickName;

    @Column(nullable = true)
    private double topAdminCommission;

    @Column(nullable = true)
    private Long agent1Id;

    @Column(nullable = true)
    private String agent1Username;

    @Column(nullable = true)
    private String agent1NickName;

    @Column(nullable = true)
    private double agent1Commission;

    @Column(nullable = true)
    private Long agent2Id;

    @Column(nullable = true)
    private String agent2Username;

    @Column(nullable = true)
    private String agent2NickName;

    @Column(nullable = true)
    private double agent2Commission;

    @Column(nullable = true)
    private Long agent3Id;

    @Column(nullable = true)
    private String agent3Username;

    @Column(nullable = true)
    private String agent3NickName;

    @Column(nullable = true)
    private double agent3Commission;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public static User create(UserPostRequestDto dto, Long requestUserId) {
        return User.builder()
                .username(dto.getUsername())
                .nickName(dto.getNickName())
                .password(dto.getPassword())
                .memo(dto.getMemo())
                .userStatus(dto.getUserStatus())
                .userLevel(dto.getUserLevel())
                .calculateBank(dto.getCalculateBank())
                .calculateAccount(dto.getCalculateAccount())
                .calculateAccountName(dto.getCalculateAccountName())
                .depositBank(dto.getDepositBank())
                .depositAccount(dto.getDepositAccount())
                .depositAccountName(dto.getDepositAccountName())
                .commission(dto.getCommission())
                .telegram(dto.getTelegram())
                .topAdminId(requestUserId)
//                .topAdminCommission(dto.getTopAdminCommission())
                .topAdminUsername("관리자")
                .topAdminNickName("관리자")
                .topAdminCommission(0.0)
                .agent1Id(dto.getAgent1Id())
                .agent1Commission(dto.getAgent1Commission())
                .agent2Id(dto.getAgent2Id())
                .agent2Commission(dto.getAgent2Commission())
                .agent3Id(dto.getAgent3Id())
                .agent3Commission(dto.getAgent3Commission())
                .balance(BigDecimal.valueOf(0))
                .totalDeposit(BigDecimal.valueOf(0))
                .adminCommissionBalance(BigDecimal.valueOf(0))
                .agent1CommissionBalance(BigDecimal.valueOf(0))
                .agent2CommissionBalance(BigDecimal.valueOf(0))
                .agent3CommissionBalance(BigDecimal.valueOf(0))
                .roles(new ArrayList<>())
                .build();
    }

    public User updateUser(UserPatchRequestDto dto) {
        Long agentOne = dto.getAgent1Id();
        Long agentTwo = dto.getAgent2Id();
        Long agentThree = dto.getAgent3Id();
        if (agentOne != null && !agentOne.equals(0L)) {
            this.agent1Id = agentOne;
        }
        if (agentTwo != null && !agentTwo.equals(0L)) {
            this.agent2Id = agentTwo;
        }
        if (agentThree != null && !agentThree.equals(0L)) {
            this.agent3Id = agentThree;
        }
        this.setUsername(dto.getUsername());
        this.setNickName(dto.getNickName());
        this.setMemo(dto.getMemo());
        this.setUserStatus(dto.getUserStatus());
        this.setUserLevel(dto.getUserLevel());
        this.setCalculateBank(dto.getCalculateBank());
        this.setCalculateAccount(dto.getCalculateAccount());
        this.setCalculateAccountName(dto.getCalculateAccountName());
        this.setDepositBank(dto.getDepositBank());
        this.setDepositAccount(dto.getDepositAccount());
        this.setDepositAccountName(dto.getDepositAccountName());
        this.setCommission(dto.getCommission());
        this.setTelegram(dto.getTelegram());
        this.setTopAdminId(dto.getTopAdminId());
        this.setTopAdminCommission(dto.getTopAdminCommission());
        this.setAgent1Commission(dto.getAgent1Commission());
        this.setAgent2Commission(dto.getAgent2Commission());
        this.setAgent3Commission(dto.getAgent3Commission());
        return this;
    }

}
