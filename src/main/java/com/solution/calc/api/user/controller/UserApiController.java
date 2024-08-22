package com.solution.calc.api.user.controller;


import com.solution.calc.api.money.dto.DepositSearchRequestDto;
import com.solution.calc.api.user.dto.*;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.domain.user.entity.User;
import com.solution.calc.domain.user.service.UserService;
import com.solution.calc.dto.PageResponseDto;
import com.solution.calc.dto.ResponseDto;
import com.solution.calc.dto.Result;
import com.solution.calc.exception.ServiceLogicException;
import com.solution.calc.utils.StubUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserApiController implements UserApiControllerIfs{

    private final UserService userService;

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<?>> getUser(Long userId) {
        UserResponseDto user = userService.findUser(userId);
        ResponseDto<UserResponseDto> response = ResponseDto.of(user, Result.ok());
        return ResponseEntity.ok().body(response);
    }


    @Override
    @GetMapping("/admin")
    public ResponseEntity<PageResponseDto<?>> getAdmin(int page) {
        Page<UserResponseDto> admin = userService.findAllUser(page);
        PageResponseDto<List<UserResponseDto>> response = PageResponseDto.of(admin, admin.getContent(), Result.ok());
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/agent")
    public ResponseEntity<PageResponseDto<?>> getAgent(int page) {
        Page<UserResponseDto> agent = userService.findAllAgent(page);
        PageResponseDto<List<UserResponseDto>> response = PageResponseDto.of(agent, agent.getContent(), Result.ok());
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/basic/{officeUserId}")
    public ResponseEntity<PageResponseDto<?>> getBasicUser(BasicUserSearchDto requestDto, int page, Long officeUserId) {
        Page<BasicUserResponseDto> basic = userService.findBasicUser(requestDto, page, officeUserId);
        PageResponseDto<List<BasicUserResponseDto>> response = PageResponseDto.of(basic, basic.getContent(), Result.ok());
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/basic")
    public ResponseEntity<PageResponseDto<?>> getAllBasicUser(BasicUserSearchDto requestDto, int page) {
        Page<BasicUserResponseDto> basic = userService.findAllBasicUser(requestDto, page);
        PageResponseDto<List<BasicUserResponseDto>> response = PageResponseDto.of(basic, basic.getContent(), Result.ok());
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/admin")
    public ResponseEntity<ResponseDto<?>> postAdmin(UserPostRequestDto requestDto, User user) {
        UserResponseDto response = userService.createUser(requestDto, user.getUserId());
        return ResponseEntity.ok(ResponseDto.of(response,Result.ok()));
    }

    @Override
    @PatchMapping("/admin/{userId}")
    public ResponseEntity<ResponseDto<?>> patchAdmin(Long userId, UserPatchRequestDto requestDto, User user) {
        UserResponseDto response = userService.updateUser(requestDto, userId);
        return ResponseEntity.ok(ResponseDto.of(response,Result.ok()));
    }

    @Override
    @PatchMapping("/basic/{basicUserId}")
    public ResponseEntity<ResponseDto<?>> patchBasicUser(Long basicUserId, BasicUserPatchRequestDto requestDto, User user) {
        BasicUserResponseDto response = userService.updateBasicUser(requestDto, basicUserId);
        return ResponseEntity.ok(ResponseDto.of(response,Result.ok()));
    }

    @Override
    @PatchMapping("/balance")
    public ResponseEntity<ResponseDto<?>> patchUserBalance(BalanceRequestDto requestDto, User user) {
        boolean result = userService.updateUserBalance(requestDto);
        if (!result) throw new ServiceLogicException(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.ok(ResponseDto.of(Result.ok()));
    }


}
