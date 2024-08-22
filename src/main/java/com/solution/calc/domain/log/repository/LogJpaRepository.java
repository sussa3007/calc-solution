package com.solution.calc.domain.log.repository;

import com.solution.calc.constant.LogType;
import com.solution.calc.domain.log.entity.LogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogJpaRepository extends JpaRepository<LogEntity, Long> {

    Page<LogEntity> findAllByActiveUsernameOrTargetUsername(String activeUsername, String targetUsername, Pageable pageable);

    Page<LogEntity> findAllByLogType(LogType logType, Pageable pageable);

}
