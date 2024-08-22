package com.solution.calc.api.money.dto;


import com.solution.calc.constant.DepositStatus;
import com.solution.calc.constant.UserLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficeLiveRequestDto {

    private Long userId;

    private UserLevel userLevel;


}
