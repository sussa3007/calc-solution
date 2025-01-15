package com.solution.calc.api.user.controller;

import com.solution.calc.annotation.UserSession;
import com.solution.calc.api.money.dto.DepositSearchRequestDto;
import com.solution.calc.api.user.dto.*;
import com.solution.calc.domain.user.entity.User;
import com.solution.calc.dto.ErrorResponse;
import com.solution.calc.dto.PageResponseDto;
import com.solution.calc.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "C. User", description = "회원 API")
@Hidden
public interface UserApiControllerIfs {

    @Operation(summary = "회원 정보 요청(관리자, 기업 - 본인 정보)", description = "특정 회원의 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<ResponseDto<?>> getUser(
            @PathVariable @Parameter(description = "회원의 식별자", required = true) Long userId
    );

    @Operation(summary = "전체 회원 정보 요청(관리자, 기업)", description = "전체 관리자, 기업 회원, 에이전시 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<PageResponseDto<?>> getAdmin(
            @RequestParam(defaultValue = "0") int page,
            @UserSession @Parameter(hidden = true) User user
    );

    @Operation(summary = "전체 에이전트 정보(관리자)", description = "전체 에이전트 정보")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<PageResponseDto<?>> getAgent(
            @RequestParam(defaultValue = "0") int page
    );

    @Operation(summary = "특정 기업 회원의 전체 일반 회원 정보 요청(기업)", description = "특정 기업 회원의 전체 일반 회원 정보 요청(officeUserId값 2 로 테스트 가능)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<PageResponseDto<?>> getBasicUser(
            @ModelAttribute BasicUserSearchDto requestDto,
            @RequestParam(defaultValue = "0") int page,
            @PathVariable @Parameter(description = "회원의 식별자", required = true) Long officeUserId
    );

    @Operation(summary = "전체 일반 회원 정보 요청(관리자)", description = "전체 일반 회원 정보 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<PageResponseDto<?>> getAllBasicUser(
            @ModelAttribute BasicUserSearchDto requestDto,
            @RequestParam(defaultValue = "0") int page
    );

    @Operation(summary = "기업, 에이전시 생성 요청(관리자)", description = "기업회원, 에이전시 생성 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<ResponseDto<?>> postAdmin(
            @RequestBody UserPostRequestDto requestDto,
            @UserSession @Parameter(hidden = true) User user
    );

    @Operation(summary = "기업, 에이전시 회원 수정 요청(관리자)", description = "기업, 에이전시 회원 수정 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<ResponseDto<?>> patchAdmin(
            @PathVariable @Parameter(description = "회원의 식별자", required = true) Long userId,
            @RequestBody UserPatchRequestDto requestDto,
            @UserSession @Parameter(hidden = true) User user
    );

    @Operation(summary = "일반 회원 수정 요청(관리자)", description = "일반 회원 수정 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<ResponseDto<?>> patchBasicUser(
            @PathVariable @Parameter(description = "일반 회원의 식별자", required = true) Long basicUserId,
            @RequestBody BasicUserPatchRequestDto requestDto,
            @UserSession @Parameter(hidden = true) User user
    );

    @Operation(summary = "기업, 에이전시 잔액 관리(관리자)", description = "기업, 에이전시 회원 잔액 수정 요청(PAYMENT, COLLECT)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<ResponseDto<?>> patchUserBalance(
            @RequestBody BalanceRequestDto requestDto,
            @UserSession @Parameter(hidden = true) User user
    );


}
