package com.solution.calc.domain.index.repository;

import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.index.entity.IndexData;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IndexDataJpaRepository extends JpaRepository<IndexData, Long> {

    Optional<IndexData> findByDateKeyAndUserLevel(String dateKey, UserLevel userLevel);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<IndexData> findByDateKey(String dateKey);

    Optional<IndexData> findByDateKeyAndUserId(String dateKey, Long userId);

    Page<IndexData> findByUserIdAndCreateAtBetween(Long userId, LocalDateTime from, LocalDateTime to, Pageable pageable);

    Page<IndexData> findByMonthAndUserId(int month ,Long userId, Pageable pageable);
}
