package com.solution.calc.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentCalculateResponseDto {

    private Long officeId;

    private String officeUsername;

    private String officeNickname;

    private List<AgentUserResponseDto> calculateData;

}
