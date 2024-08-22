package com.solution.calc.api.money.dto;

import com.solution.calc.constant.CalculateStatus;
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
public class CalculateSearchRequestDto {

    private String username;

    private String nickName;

    private LocalDate startAt;

    private LocalDate endAt;

    private CalculateStatus calculateStatus;
}
