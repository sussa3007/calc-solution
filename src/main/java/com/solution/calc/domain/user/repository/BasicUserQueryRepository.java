package com.solution.calc.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.solution.calc.api.money.dto.DepositResponseDto;
import com.solution.calc.api.user.dto.BasicUserResponseDto;
import com.solution.calc.api.user.dto.BasicUserSearchDto;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.constant.UserStatus;
import com.solution.calc.domain.money.entity.CalculateData;
import com.solution.calc.domain.user.entity.BasicUser;
import com.solution.calc.domain.user.entity.QBasicUser;
import com.solution.calc.exception.ServiceLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BasicUserQueryRepository extends QuerydslRepositorySupport {

    public BasicUserQueryRepository() {
        super(BasicUser.class);
    }

    QBasicUser basicUser = QBasicUser.basicUser;
    
    public Page<BasicUserResponseDto> findAllByOfficeId(BasicUserSearchDto dto, Long officeId, Pageable pageable) {
        String account = dto.getAccount();
        String nickName = dto.getNickName();
        UserStatus userStatus = dto.getUserStatus();
        String officeUsernameOrNickName = dto.getOfficeUsernameOrNickName();
        JPQLQuery<BasicUserResponseDto> query = getBasicUserQuery();

        if (officeUsernameOrNickName != null) {
            query.where(
                    basicUser.officeUsername.containsIgnoreCase(officeUsernameOrNickName)
                            .or(basicUser.officeNickName.containsIgnoreCase(officeUsernameOrNickName))
            );
        }
        if (userStatus != null) {
            query.where(
                    basicUser.userStatus.eq(userStatus)
            );
        }
        if (nickName != null) {
            query.where(
                    basicUser.nickName.containsIgnoreCase(nickName)
            );
        }
        if (account != null) {
            query.where(
                    basicUser.account.containsIgnoreCase(account)
            );
        }
        if (officeId != null) {
            query.where(
                    basicUser.officeId.eq(officeId)
            );
        }
        List<BasicUserResponseDto> list = Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.DATA_ACCESS_ERROR))
                .applyPagination(pageable, query)
                .fetch();
        return new PageImpl<>(list, pageable, query.fetchCount());
    }
    
    public Page<BasicUserResponseDto> findAll(BasicUserSearchDto dto, Pageable pageable) {

        String account = dto.getAccount();
        String nickName = dto.getNickName();
        UserStatus userStatus = dto.getUserStatus();
        String officeUsernameOrNickName = dto.getOfficeUsernameOrNickName();
        JPQLQuery<BasicUserResponseDto> query = getBasicUserQuery();

        if (officeUsernameOrNickName != null) {
            query.where(
                    basicUser.officeUsername.containsIgnoreCase(officeUsernameOrNickName)
                            .or(basicUser.officeNickName.containsIgnoreCase(officeUsernameOrNickName))
            );
        }
        if (userStatus != null) {
            query.where(
                    basicUser.userStatus.eq(userStatus)
            );
        }
        if (nickName != null) {
            query.where(
                    basicUser.nickName.containsIgnoreCase(nickName)
            );
        }
        if (account != null) {
            query.where(
                    basicUser.account.containsIgnoreCase(account)
            );
        }
        List<BasicUserResponseDto> list = Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.DATA_ACCESS_ERROR))
                .applyPagination(pageable, query)
                .fetch();
        return new PageImpl<>(list, pageable, query.fetchCount());
    }

    private JPQLQuery<BasicUserResponseDto> getBasicUserQuery() {
        return from(basicUser)
                .select(
                        Projections.constructor(
                                BasicUserResponseDto.class,
                                basicUser.basicUserId,
                                basicUser.username,
                                basicUser.nickName,
                                basicUser.bank,
                                basicUser.account,
                                basicUser.userStatus,
                                basicUser.officeId,
                                basicUser.officeUsername,
                                basicUser.officeNickName,
                                basicUser.totalDepositBalance,
                                basicUser.createAt
                        )
                );
    }
}
