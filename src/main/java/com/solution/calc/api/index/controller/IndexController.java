package com.solution.calc.api.index.controller;


import com.solution.calc.api.index.dto.IndexResponseDto;
import com.solution.calc.api.index.dto.IndexSearchDto;
import com.solution.calc.api.user.dto.AgentCalculateResponseDto;
import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.index.service.IndexService;
import com.solution.calc.domain.user.entity.User;
import com.solution.calc.dto.PageResponseDto;
import com.solution.calc.dto.ResponseDto;
import com.solution.calc.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/index")
@RequiredArgsConstructor
@Slf4j
public class IndexController implements IndexControllerIfs {

    private final IndexService indexService;


    @Override
    @GetMapping("/data")
    public ResponseEntity<ResponseDto<?>> getIndex(User user) {
        IndexResponseDto response = indexService.findIndex(user.getUserId(), user.getUserLevel());
        return ResponseEntity.ok(ResponseDto.of(response, Result.ok()));
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<PageResponseDto<?>> getIndexList(IndexSearchDto requestDto, User user) {
        Page<IndexResponseDto> response = indexService.findIndexList(user.getUserId(), requestDto);
        return ResponseEntity.ok(PageResponseDto.of(response, response.getContent(), Result.ok()));
    }

    @Override
    @GetMapping("/agent/list")
    public ResponseEntity<PageResponseDto<?>> getAgentIndexList(IndexSearchDto requestDto, User user) {
        Page<AgentCalculateResponseDto> response = indexService.findAgentIndexList(user.getUserId(), requestDto);
        return ResponseEntity.ok(PageResponseDto.of(response, response.getContent(), Result.ok()));
    }
}
