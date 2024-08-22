package com.solution.calc.domain.user.entity;


import com.solution.calc.api.user.dto.BasicUserPatchRequestDto;
import com.solution.calc.audit.Auditable;
import com.solution.calc.constant.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "basic_users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicUser extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basicUserId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String bank;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(nullable = false)
    private Long officeId;

    @Column(nullable = false)
    private String officeUsername;

    @Column(nullable = false)
    private String officeNickName;

    @Column(nullable = false)
    private BigDecimal totalDepositBalance;


    public BasicUser update(BasicUserPatchRequestDto dto) {
        if (dto.getUserStatus() != null) {
            this.setUserStatus(dto.getUserStatus());
        }
        this.setNickName(dto.getNickName());
        this.setBank(dto.getBank());
        this.setAccount(dto.getAccount());
        return this;
    }

    public static BasicUser create(String username, String nickName, String depositBank, String depositAccount, Long officeId) {
        return BasicUser.builder()
                .username(username)
                .nickName(nickName)
                .bank(depositBank)
                .account(depositAccount)
                .officeId(officeId)
                .userStatus(UserStatus.ACTIVE)
                .totalDepositBalance(BigDecimal.valueOf(0))
                .build();
    }

}
