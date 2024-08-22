package com.solution.calc.api.index.controller;

import com.solution.calc.annotation.UserSession;
import com.solution.calc.api.index.dto.IndexSearchDto;
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

@Tag(name = "B. Index", description = "회원 API")
@Hidden
public interface IndexControllerIfs {

    @Operation(summary = "데시보드 데이터 조회(관리자, 기업)", description = "입/출금 정산 현황 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<ResponseDto<?>> getIndex(
            @UserSession @Parameter(hidden = true) User user
    );

    @Operation(summary = "통계 데이터 리스트 조회(관리자, 기업)", description = "입/출금 정산 현황 리스트 조회, 기본 조회시 이번달 데이터 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<PageResponseDto<?>> getIndexList(
            @ModelAttribute IndexSearchDto requestDto,
            @UserSession @Parameter(hidden = true) User user
    );

    @Operation(summary = "에이전트 통계 데이터 리스트 조회(관리자, 기업)", description = "에이전트 통계 데이터 리스트 조회, 기본 조회시 이번달 데이터 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<PageResponseDto<?>> getAgentIndexList(
            @ModelAttribute IndexSearchDto requestDto,
            @UserSession @Parameter(hidden = true) User user
    );
}
