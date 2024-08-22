package com.solution.calc.api.money.controller.deposit;


import com.solution.calc.annotation.UserSession;
import com.solution.calc.api.money.dto.*;
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

import java.util.List;

@Tag(name = "B. Deposit", description = "입금 API")
public interface DepositApiControllerIfs {

    // 전체 입금 데이터 조회
    @Operation(summary = "전체 입금 데이터 조회(관리자, 기업)",
            description = """
                    관리자의 전체 입금 내역 데이터 전체 조회
                    - depositStatus
                      - WAIT - 대기
                      - DEPOSIT_COMPLETE - 입금 확인 완료
                      - COMPLETE - 처리 완료
                      - CANCEL - 취소
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Hidden
    ResponseEntity<PageResponseDto<?>> getAdminDeposit(
            @ModelAttribute DepositSearchRequestDto requestDto,
            @RequestParam(defaultValue = "0") int page,
            @UserSession @Parameter(hidden = true) User user
    );

    // 실시간 입금 내역 데이터 전체 조회(입금 완료, 대기 상태 데이터 응답)
    @Operation(summary = "실시간 전체 입금 데이터 조회(관리자, 기업)", description = """
            관리자/기업회원 실시간 전체 입금 내역 데이터 전체 조회
            - depositStatus
              - WAIT - 대기
              - DEPOSIT_COMPLETE - 입금 확인 완료
              - COMPLETE - 처리 완료
              - CANCEL - 취소
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Hidden
    ResponseEntity<PageResponseDto<?>> getLiveDeposit(
            @RequestParam(defaultValue = "0") int page,
            @UserSession @Parameter(hidden = true) User user
    );


    @Operation(summary = "실시간 기업별 입금 데이터 조회(관리자, 기업)", description = """
            관리자/기업회원 실시간 전체 입금 내역 데이터 전체 조회
            - depositStatus
              - WAIT - 대기
              - DEPOSIT_COMPLETE - 입금 확인 완료
              - COMPLETE - 처리 완료
              - CANCEL - 취소
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Hidden
    ResponseEntity<PageResponseDto<?>> getLiveOfficeDeposit(
            @RequestParam(defaultValue = "0") int page,
            @ModelAttribute OfficeLiveRequestDto requestDto
    );


    // 특정 입금 요청 조회
    @Operation(summary = "특정 입금 데이터 조회(관리자, 기업)", description = "특정 일반 회원 입금 데이터 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Hidden
    ResponseEntity<ResponseDto<?>> getDeposit(
            @PathVariable @Parameter(description = "입금 내역 데이터 식별자", required = true) Long dataId,
            @UserSession @Parameter(hidden = true) User user
    );

    @Operation(summary = "특정 입금 데이터 TXNID 조회(관리자, 기업)", description = """
                    특정 입금 데이터 TXNID 식별자 조회
                    - depositStatus
                      - WAIT - 대기
                      - DEPOSIT_COMPLETE - 입금 확인 완료
                      - COMPLETE - 모든 처리 완료
                      - CANCEL - 취소
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json"
                            , schema = @Schema(implementation = DepositResponse.class)
                    )}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<ResponseDto<?>> getDepositByTxnId(
            @PathVariable @Parameter(description = "입금 내역 데이터 TxnID", required = true) String txnId,
            @UserSession @Parameter(hidden = true) User user
    );

    // 입금 요청 생성
    class DepositResponse extends ResponseDto<DepositResponseDto> {
    }
    @Operation(summary = "입금 데이터 생성(외부)", description = """
            - 로그인 요청 후 얻은 Access Token 값 헤더에 넣어 요청
            - 로그인 시 발급 받은 Access Token 만료 기한 30일
            - 만료시 응답 : 403 EXPIRED_ACCESS_TOKEN
            - Header Key : Authorization
            - EX) Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.hsakljfh304JFDSHF.......
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json"
                            , schema = @Schema(implementation = DepositResponse.class)
                    )})
//            ,
//            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
//                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
//                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<ResponseDto<?>> postDeposit(
            @RequestBody DepositPostRequestDto requestDto,
            @UserSession @Parameter(hidden = true) User user
    );

    // 입금 요청 수정/취소

    @Operation(summary = "특정 입금 데이터 수정(관리자)", description = "특정 일반 회원 입금 데이터 수정 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Hidden
    ResponseEntity<ResponseDto<?>> patchDeposit(
            @RequestBody DepositPatchRequestDto requestDto,
            @UserSession @Parameter(hidden = true) User user
    );

    class DepositPageResponse extends PageResponseDto<List<DepositResponseDto>> {

    }
    // 특정 회원 최근 입금 내역 조회
    @Operation(summary = "일반회원 최근 입금 데이터 조회(관리자, 기업)", description = """
            특정 일반 회원 최근 입금 내역

            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = {@Content(mediaType = "application/json"
                            , schema = @Schema(implementation = DepositPageResponse.class)
                    )}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Hidden
    ResponseEntity<PageResponseDto<?>> getBasicUserDeposit(
            @PathVariable @Parameter(description = "일반 회원 Username", required = true) String basicUsername,
            @RequestParam(defaultValue = "0") int page,
            @UserSession @Parameter(hidden = true) User user
    );

}
