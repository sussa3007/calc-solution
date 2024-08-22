package com.solution.calc.api.user.dto;

import com.solution.calc.constant.UserStatus;
import com.solution.calc.domain.user.entity.BasicUser;
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
public class BasicUserResponseDto {

    private Long basicUserId;

    private String username;

    private String nickName;

    private String bank;

    private String account;

    private UserStatus userStatus;

    private Long officeId;

    private String officeUsername;

    private String officeNickName;

    private BigDecimal totalDepositBalance;

    private LocalDateTime createAt;

    public static BasicUserResponseDto of(BasicUser user) {
        return BasicUserResponseDto.builder()
                .basicUserId(user.getBasicUserId())
                .username(user.getUsername())
                .nickName(user.getNickName())
                .bank(user.getBank())
                .account(user.getAccount())
                .userStatus(user.getUserStatus())
                .officeId(user.getOfficeId())
                .officeUsername(user.getOfficeUsername())
                .officeNickName(user.getOfficeNickName())
                .totalDepositBalance(user.getTotalDepositBalance())
                .createAt(user.getCreateAt())
                .build();
    }

}
