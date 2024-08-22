package com.solution.calc.api.index.dto;

import com.solution.calc.constant.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexSearchDto {

    private LocalDate startDate;

    private LocalDate endDate;

    private int page;

}
