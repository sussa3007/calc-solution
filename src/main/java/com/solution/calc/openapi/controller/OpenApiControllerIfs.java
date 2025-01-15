package com.solution.calc.openapi.controller;


import com.solution.calc.annotation.UserSession;
import com.solution.calc.api.money.dto.DepositExcelSearchRequestDto;
import com.solution.calc.domain.user.entity.User;
import com.solution.calc.dto.ErrorResponse;
import com.solution.calc.dto.ResponseDto;
import com.solution.calc.openapi.dto.LoginApiResponseDto;
import com.solution.calc.openapi.dto.LoginDto;
import com.solution.calc.openapi.dto.TokenApiResponseDto;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "A. OPEN API", description = "OPEN API(권한 없이 요청 가능)")
public interface OpenApiControllerIfs {

    class LoginResponse extends ResponseDto<LoginApiResponseDto> {}
    class TokenResponse extends ResponseDto<TokenApiResponseDto> {}

    @Operation(summary = "로그인 요청", description = "바디 데이터 응답 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "ACCESS DENIED(아이디 혹은 비밀번호 틀림)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<?> login(
            @RequestBody LoginDto loginDto
    );

    @Operation(summary = "토큰 요청", description = "바디 데이터 응답 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "ACCESS DENIED(아이디 혹은 비밀번호 틀림)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Hidden
    ResponseEntity<?> loginApi(
            @RequestBody LoginDto loginDto
    );


    @Hidden
    void excelDownLoad(
            @ModelAttribute DepositExcelSearchRequestDto requestDto,
            HttpServletResponse response
    );

}
