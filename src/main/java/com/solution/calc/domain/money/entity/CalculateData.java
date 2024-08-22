package com.solution.calc.domain.money.entity;

import com.solution.calc.api.money.dto.CalculatePostRequestDto;
import com.solution.calc.api.user.dto.UserResponseDto;
import com.solution.calc.audit.Auditable;
import com.solution.calc.constant.CalculateStatus;
import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "calculate_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculateData extends Auditable {

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
    private String calculateBank;

    @Column(nullable = false)
    private String calculateAccount;

    @Column(nullable = false)
    private Long topAdminId;

    @Column(nullable = false)
    private String topAdminUsername;

    @Column(nullable = false)
    private String topAdminNickName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CalculateStatus calculateStatus;

    @Column(nullable = false)
    private BigDecimal calculateBalance;

    @Column(nullable = true)
    private LocalDateTime completeAt;

    public static CalculateData create(CalculatePostRequestDto dto, UserResponseDto office) {
        if (office.getUserLevel().equals(UserLevel.ADMIN)) {
            return CalculateData.builder()
                    .officeId(office.getUserId())
                    .officeUsername(office.getUsername())
                    .officeNickName(office.getNickName())
                    .calculateBank("관리자 정산")
                    .calculateAccount("관리자 정산")
                    .topAdminId(office.getUserId())
                    .topAdminUsername("관리자 정산")
                    .topAdminNickName("관리자 정산")
                    .calculateStatus(CalculateStatus.WAIT)
                    .calculateBalance(dto.getCalculateBalance())
                    .build();
        }
        return CalculateData.builder()
                .officeId(office.getUserId())
                .officeUsername(office.getUsername())
                .officeNickName(office.getNickName())
                .calculateBank(office.getCalculateBank())
                .calculateAccount(office.getCalculateAccount())
                .topAdminId(office.getTopAdminId())
                .topAdminUsername(office.getTopAdminUsername())
                .topAdminNickName(office.getTopAdminNickName())
                .calculateStatus(CalculateStatus.WAIT)
                .calculateBalance(dto.getCalculateBalance())
                .build();
    }

}
