package com.solution.calc.api.money.controller.calculate;

import com.solution.calc.api.money.dto.CalculatePatchRequestDto;
import com.solution.calc.api.money.dto.CalculatePostRequestDto;
import com.solution.calc.api.money.dto.CalculateResponseDto;
import com.solution.calc.api.money.dto.CalculateSearchRequestDto;
import com.solution.calc.constant.CalculateStatus;
import com.solution.calc.domain.money.service.CalculateService;
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
@RequestMapping("/api/calc")
@RequiredArgsConstructor
@Slf4j
public class CalculateApiController implements CalculateApiControllerIfs {

    private final CalculateService calculateService;

    @Override
    @GetMapping
    public ResponseEntity<PageResponseDto<?>> getCalculate(CalculateSearchRequestDto requestDto, int page, User user) {
        Page<CalculateResponseDto> response = calculateService.findAllCalculate(requestDto, page, user.getUserId(), user.getUserLevel());

        return ResponseEntity.ok(PageResponseDto.of(response, response.getContent(), Result.ok()));
    }

    @Override
    @PostMapping
    public ResponseEntity<ResponseDto<?>> postCalculate(CalculatePostRequestDto requestDto, User user) {
        CalculateResponseDto response = calculateService.createCalculate(requestDto, user.getUserId(), user.getUserLevel());
        return ResponseEntity.ok(ResponseDto.of(response, Result.ok()));
    }

    @Override
    @PatchMapping
    public ResponseEntity<ResponseDto<?>> patchCalculate(CalculatePatchRequestDto requestDto, User user) {
        CalculateResponseDto response = calculateService.updateCalculate(requestDto, user.getUserId(), user.getUserLevel());
        return ResponseEntity.ok(ResponseDto.of(response, Result.ok()));
    }
}
