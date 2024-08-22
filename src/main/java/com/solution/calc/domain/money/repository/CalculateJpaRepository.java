package com.solution.calc.domain.money.repository;

import com.solution.calc.constant.CalculateStatus;
import com.solution.calc.domain.money.entity.CalculateData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalculateJpaRepository extends JpaRepository<CalculateData, Long> {

    Optional<CalculateData> findByOfficeIdAndCalculateStatus(Long officeId, CalculateStatus calculateStatus);
}
