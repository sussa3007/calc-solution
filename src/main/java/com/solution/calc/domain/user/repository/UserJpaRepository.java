package com.solution.calc.domain.user.repository;

import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findByUserId(Long userId);


    Optional<User> findByUsername(String username);


    Page<User> findAllByUserLevel(UserLevel userLevel, Pageable pageable);
}
