package com.solution.calc.api.money.controller.calculate;

import com.solution.calc.annotation.UserSession;
import com.solution.calc.api.money.dto.CalculatePatchRequestDto;
import com.solution.calc.api.money.dto.CalculatePostRequestDto;
import com.solution.calc.api.money.dto.CalculateSearchRequestDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "D. Calculate", description = "정산(출금) API")
@Hidden
public interface CalculateApiControllerIfs {

    // 정산 데이터 전체 조회
    @Operation(summary = "정산 데이터 전체 조회 요청(관리자, 기업)", description = "정산(출금) 내역 데이터 전체 조회 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<PageResponseDto<?>> getCalculate(
            @ModelAttribute CalculateSearchRequestDto requestDto,
            @RequestParam(defaultValue = "0") int page,
            @UserSession @Parameter(hidden = true) User user
    );

    // 정산 생성 요청(외부 API)
    @Operation(summary = "정산 데이터 생성 요청(기업, 에이전트)", description = "정산(입금) 요청 데이터 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<ResponseDto<?>> postCalculate(
            @RequestBody CalculatePostRequestDto requestDto,
            @UserSession @Parameter(hidden = true) User user
    );

    // 정산 데이터 수정/취소 요청
    @Operation(summary = "정산 데이터 수정 요청(관리자)", description = "정산(입금) 요청 데이터 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<ResponseDto<?>> patchCalculate(
            @RequestBody CalculatePatchRequestDto requestDto,
            @UserSession @Parameter(hidden = true) User user
    );

}
