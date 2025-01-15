package com.solution.calc.domain.rtpay.repository;

import com.solution.calc.domain.rtpay.entity.RtpayKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RtpayKeyJpaRepository extends JpaRepository<RtpayKey, Long> {
    Optional<RtpayKey> findByRtpayKey(String key);

    Optional<RtpayKey> findByOfficeUsername(String username);
}
