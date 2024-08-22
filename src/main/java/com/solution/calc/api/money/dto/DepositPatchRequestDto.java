package com.solution.calc.api.money.dto;

import com.solution.calc.constant.DepositStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositPatchRequestDto {

    private Long depositDataId;

    private DepositStatus depositStatus;
}
