package com.solution.calc.openapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AutoDepositRequestDto {

    private String username;

    private String password;

    private String depositName;

    private String balance;

}