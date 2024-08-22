package com.solution.calc.api.user.dto;

import com.solution.calc.constant.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicUserSearchDto {

    private String nickName;

    private String officeUsernameOrNickName;

    private UserStatus userStatus;

    private String account;
}
