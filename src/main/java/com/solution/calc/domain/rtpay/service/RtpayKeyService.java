package com.solution.calc.domain.rtpay.service;

import com.solution.calc.constant.ErrorCode;
import com.solution.calc.domain.rtpay.entity.RtpayKey;
import com.solution.calc.domain.rtpay.repository.RtpayKeyJpaRepository;
import com.solution.calc.exception.ServiceLogicException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RtpayKeyService {

    private final RtpayKeyJpaRepository rtpayKeyJpaRepository;

    public String getOffice(String rtpayKey) {
        return rtpayKeyJpaRepository.findByRtpayKey(rtpayKey)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND)).getOfficeUsername();

    }
    public Optional<RtpayKey> getOpOffice(String rtpayKey) {
        return rtpayKeyJpaRepository.findByRtpayKey(rtpayKey);

    }


    public String getRtpayKey(String office) {
        Optional<RtpayKey> byOfficeUsername = rtpayKeyJpaRepository.findByOfficeUsername(office);
        if (byOfficeUsername.isPresent()) {
            return byOfficeUsername.get().getRtpayKey();
        } else {
            return "";
        }
    }

    public boolean createAndUpdateRtpayKey(String office, String rtpayKey) {
        Optional<RtpayKey> byOfficeUsername = rtpayKeyJpaRepository.findByOfficeUsername(office);
        if (byOfficeUsername.isPresent()) {
            try {
                RtpayKey findRtpay = byOfficeUsername.get();
                findRtpay.setRtpayKey(rtpayKey);
                rtpayKeyJpaRepository.save(findRtpay);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            try {
                RtpayKey save = rtpayKeyJpaRepository.save(RtpayKey.builder().officeUsername(office).rtpayKey(rtpayKey).build());
                return true;
            } catch (Exception e) {
                return false;
            }
        }

    }
}
