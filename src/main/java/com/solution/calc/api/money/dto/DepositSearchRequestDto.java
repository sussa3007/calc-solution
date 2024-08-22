package com.solution.calc.api.money.dto;


import com.solution.calc.constant.DepositStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositSearchRequestDto {

    private String username;

    private String nickName;

    private LocalDate startAt;

    private LocalDate endAt;

    private DepositStatus depositStatus;


}
