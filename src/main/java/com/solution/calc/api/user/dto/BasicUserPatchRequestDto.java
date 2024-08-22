package com.solution.calc.api.user.dto;

import com.solution.calc.constant.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "일반 회원 수정 요청 데이터")
public class BasicUserPatchRequestDto {

    private String nickName;

    private String bank;

    private String account;

    private UserStatus userStatus;

}
