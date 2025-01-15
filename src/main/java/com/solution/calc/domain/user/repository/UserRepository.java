package com.solution.calc.domain.user.repository;

import com.solution.calc.api.user.dto.BasicUserResponseDto;
import com.solution.calc.api.user.dto.BasicUserSearchDto;
import com.solution.calc.api.user.dto.UserResponseDto;
import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.user.entity.BasicUser;
import com.solution.calc.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository {

    User saveUser(User user);

    BasicUser saveBasicUser(BasicUser basicUser);

    Optional<User> findUserByUserId(Long userId);

    Optional<User> findUserByUserIdForUpdate(Long userId);

    Optional<User> findUserByUsername(String username);

    Page<User> findAllAdmin(Pageable pageable);
    Page<User> findAllAdmin(Pageable pageable, UserLevel userLevel, Long userId);

    Page<User> findAllAgent(Pageable pageable);

    Page<BasicUserResponseDto> findAllBasicUserByOffice(BasicUserSearchDto dto, Long officeId, Pageable pageable);

    Page<BasicUserResponseDto> findAllBasicUser(BasicUserSearchDto dto, Pageable pageable);

    Optional<BasicUser> findBasicUserById(Long basicUserId);

    Optional<BasicUser> findBasicUserParam(String username, Long officeId);

    Page<UserResponseDto> findAgentOffice(Long agentId, Pageable pageable);


}
