package com.solution.calc.api.money.dto;

import com.solution.calc.constant.CalculateStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculatePatchRequestDto {

    private Long calculateDataId;

    private CalculateStatus calculateStatus;

}
