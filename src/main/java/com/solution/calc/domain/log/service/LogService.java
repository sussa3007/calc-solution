package com.solution.calc.domain.log.service;

import com.solution.calc.constant.LogType;
import com.solution.calc.domain.log.entity.LogEntity;
import com.solution.calc.domain.log.repository.LogJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LogService {

    private final LogJpaRepository logJpaRepository;

    public Long saveMoneyLog(
            String activeUser,
            String targetUser,
            LogType logType,
            long balance,
            long targetUserBalance,
            long resultTargetUserBalance
    ) {
        String message = LogType.createMoneyLog(
                activeUser,
                targetUser,
                logType,
                balance,
                targetUserBalance,
                resultTargetUserBalance
        );
        LogEntity log = LogEntity.builder()
                .logType(logType)
                .message(message)
                .activeUsername(activeUser)
                .targetUsername(targetUser)
                .build();
        LogEntity saveLog = logJpaRepository.save(log);
        return saveLog.getLogId();
    }

    public Long saveCancelLog(
            String activeUser,
            LogType logType,
            String  targetUsername,
            String txnid
    ) {
        String message = LogType.createCancelLog(
                activeUser,
                logType,
                targetUsername,
                txnid
        );
        LogEntity log = LogEntity.builder()
                .logType(logType)
                .message(message)
                .activeUsername(activeUser)
                .targetUsername(targetUsername)
                .build();
        LogEntity saveLog = logJpaRepository.save(log);
        return saveLog.getLogId();
    }
}
