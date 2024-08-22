package com.solution.calc.domain.money.entity;


import com.solution.calc.api.money.dto.DepositPostRequestDto;
import com.solution.calc.api.user.dto.UserResponseDto;
import com.solution.calc.audit.Auditable;
import com.solution.calc.constant.CalculateStatus;
import com.solution.calc.constant.DepositStatus;
import com.solution.calc.domain.user.entity.BasicUser;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "deposit_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositData extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataId;

    @Column(nullable = false)
    private Long officeId;

    @Column(nullable = false)
    private String officeUsername;

    @Column(nullable = false)
    private String officeNickName;

    @Column(nullable = false)
    private Long topAdminId;

    @Column(nullable = false)
    private String topAdminUsername;

    @Column(nullable = false)
    private String topAdminNickName;

    @Column(nullable = false)
    private Long basicUserId;

    @Column(nullable = false)
    private String basicUsername;

    @Column(nullable = false)
    private String basicUserNickName;

    @Column(nullable = false)
    private String depositBank;

    @Column(nullable = false)
    private String depositAccount;

    @Column(nullable = false)
    private String officeDepositBank;

    @Column(nullable = false)
    private String officeDepositAccount;

    @Column(nullable = false)
    private String officeDepositName;

    @Column(nullable = false, unique = true)
    private String txnId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DepositStatus depositStatus;

    @Column(nullable = false)
    private BigDecimal depositBalance;

    @Column(nullable = false)
    private BigDecimal resultCommission;

    @Column(nullable = false)
    private BigDecimal resultBalance;

    @Column(nullable = true)
    private LocalDateTime completeAt;

    public static DepositData create(DepositPostRequestDto dto, BasicUser basicUser,  UserResponseDto office) {
        return DepositData.builder()
                .officeId(office.getUserId())
                .officeUsername(office.getUsername())
                .officeNickName(office.getNickName())
                .topAdminId(office.getTopAdminId())
                .topAdminUsername(office.getTopAdminUsername())
                .topAdminNickName(office.getTopAdminNickName())
                .basicUserId(basicUser.getBasicUserId())
                .basicUsername(basicUser.getUsername())
                .basicUserNickName(basicUser.getNickName())
                .depositBank(dto.getDepositBank())
                .depositAccount(dto.getDepositAccount())
                .officeDepositBank(office.getDepositBank())
                .officeDepositAccount(office.getDepositAccount())
                .officeDepositName(office.getDepositAccountName())
                .txnId(UUID.randomUUID().toString())
                .depositStatus(DepositStatus.WAIT)
                .resultBalance(BigDecimal.valueOf(0))
                .resultCommission(BigDecimal.valueOf(0))
                .depositBalance(dto.getDepositBalance())
                .build();
    }
}
