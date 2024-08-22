package com.solution.calc.api.money.controller.deposit;

import com.solution.calc.api.money.dto.*;
import com.solution.calc.domain.money.service.DepositService;
import com.solution.calc.domain.user.entity.User;
import com.solution.calc.dto.PageResponseDto;
import com.solution.calc.dto.ResponseDto;
import com.solution.calc.dto.Result;
import com.solution.calc.utils.StubUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deposit")
@RequiredArgsConstructor
@Slf4j
public class DepositApiController implements DepositApiControllerIfs {

    private final StubUtils stubUtils;

    private final DepositService depositService;


    @Override
    @GetMapping("/admin")
    public ResponseEntity<PageResponseDto<?>> getAdminDeposit(DepositSearchRequestDto requestDto, int page, User user) {
        Page<DepositResponseDto> response = depositService.findAllDeposit(
                requestDto,
                page,
                user.getUserId(),
                user.getUserLevel()
        );
        return ResponseEntity.ok(PageResponseDto.of(response,response.getContent(), Result.ok()));
    }

    @Override
    @GetMapping("/live")
    public ResponseEntity<PageResponseDto<?>> getLiveDeposit(int page, User user) {
        Page<DepositResponseDto> response = depositService.findLiveDeposit(page, user.getUserId(), user.getUserLevel());
        return ResponseEntity.ok(PageResponseDto.of(response,response.getContent(), Result.ok()));
    }

    @Override
    @GetMapping("/live-office")
    public ResponseEntity<PageResponseDto<?>> getLiveOfficeDeposit(int page, OfficeLiveRequestDto requestDto) {
        Page<DepositResponseDto> response = depositService.findLiveDeposit(page, requestDto.getUserId(), requestDto.getUserLevel());
        return ResponseEntity.ok(PageResponseDto.of(response,response.getContent(), Result.ok()));
    }


    @Override
    @GetMapping("/{dataId}")
    public ResponseEntity<ResponseDto<?>> getDeposit(Long dataId, User user) {
        DepositResponseDto response = depositService.findDeposit(dataId);
        return ResponseEntity.ok(ResponseDto.of(response, Result.ok()));
    }

    @Override
    @GetMapping("/tx/{txnId}")
    public ResponseEntity<ResponseDto<?>> getDepositByTxnId(String txnId, User user) {
        DepositResponseDto response = depositService.findDepositByTx(txnId);
        return ResponseEntity.ok(ResponseDto.of(response, Result.ok()));
    }

    @Override
    @PostMapping
    public ResponseEntity<ResponseDto<?>> postDeposit(DepositPostRequestDto requestDto, User user) {
        DepositResponseDto response = depositService.createDeposit(requestDto, user.getUserId(), user.getUserLevel());
        return ResponseEntity.ok(ResponseDto.of(response, Result.ok()));
    }

    @Override
    @PatchMapping
    public ResponseEntity<ResponseDto<?>> patchDeposit(DepositPatchRequestDto requestDto, User user) {
        DepositResponseDto response = depositService.patchDeposit(requestDto, user.getUserId(), user.getUserLevel());
        return ResponseEntity.ok(ResponseDto.of(response, Result.ok()));
    }

    @Override
    @GetMapping("/b-user/{basicUsername}")
    public ResponseEntity<PageResponseDto<?>> getBasicUserDeposit(String basicUsername, int page,User user) {
        Page<DepositResponseDto> response = depositService.findBasicUserDeposit(page, basicUsername);
        return ResponseEntity.ok(PageResponseDto.of(response,response.getContent(), Result.ok()));
    }


}
