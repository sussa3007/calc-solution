package com.solution.calc.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.solution.calc.api.money.dto.CalculateResponseDto;
import com.solution.calc.api.user.dto.BasicUserResponseDto;
import com.solution.calc.api.user.dto.BasicUserSearchDto;
import com.solution.calc.api.user.dto.UserResponseDto;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.constant.UserLevel;
import com.solution.calc.domain.user.entity.BasicUser;
import com.solution.calc.domain.user.entity.QUser;
import com.solution.calc.domain.user.entity.User;
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
public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepository{

    private final UserJpaRepository userJpaRepository;

    private final BasicUserJpaRepository basicUserJpaRepository;

    private final BasicUserQueryRepository basicUserQueryRepository;
    public UserRepositoryImpl(UserJpaRepository userJpaRepository, BasicUserJpaRepository basicUserJpaRepository, BasicUserQueryRepository basicUserQueryRepository) {
        super(User.class);
        this.userJpaRepository = userJpaRepository;
        this.basicUserJpaRepository = basicUserJpaRepository;
        this.basicUserQueryRepository = basicUserQueryRepository;
    }

    QUser user = QUser.user;

    @Override
    public User saveUser(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public BasicUser saveBasicUser(BasicUser basicUser) {
        return basicUserJpaRepository.save(basicUser);
    }

    @Override
    public Optional<User> findUserByUserId(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> findUserByUserIdForUpdate(Long userId) {
        return userJpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public Page<User> findAllAdmin(Pageable pageable) {
        return userJpaRepository.findAll(pageable);
    }

    @Override
    public Page<User> findAllAgent(Pageable pageable) {
        return userJpaRepository.findAllByUserLevel(UserLevel.AGENT, pageable);
    }

    @Override
    public Page<BasicUserResponseDto> findAllBasicUserByOffice(BasicUserSearchDto dto, Long officeId, Pageable pageable) {
        return basicUserQueryRepository.findAllByOfficeId(dto, officeId, pageable);
    }

    @Override
    public Page<BasicUserResponseDto> findAllBasicUser(BasicUserSearchDto dto, Pageable pageable) {
        return basicUserQueryRepository.findAll(dto, pageable);
    }

    @Override
    public Optional<BasicUser> findBasicUserById(Long basicUserId) {
        return basicUserJpaRepository.findById(basicUserId);
    }

    @Override
    public Optional<BasicUser> findBasicUserParam(String username, Long officeId) {
        return basicUserJpaRepository.findByUsernameAndOfficeId(
                username,
                officeId
        );
    }

    @Override
    public Page<UserResponseDto> findAgentOffice(Long agentId, Pageable pageable) {
        JPQLQuery<UserResponseDto> query = getUserQuery();
        query.where(
                user.agent1Id.eq(agentId).or(user.agent2Id.eq(agentId)).or(user.agent3Id.eq(agentId))
        );

        List<UserResponseDto> list = Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.DATA_ACCESS_ERROR))
                .applyPagination(pageable, query)
                .fetch();
        return new PageImpl<>(list, pageable, query.fetchCount());
    }

    private JPQLQuery<UserResponseDto> getUserQuery() {
        return from(user)
                .select(
                        Projections.constructor(
                                UserResponseDto.class,
                                user.userId,
                                user.username,
                                user.nickName,
                                user.memo,
                                user.userStatus,
                                user.userLevel,
                                user.calculateBank,
                                user.calculateAccount,
                                user.calculateAccountName,
                                user.depositBank,
                                user.depositAccount,
                                user.depositAccountName,
                                user.commission,
                                user.totalCommission,
                                user.telegram,
                                user.balance,
                                user.totalDeposit,
                                user.adminCommissionBalance,
                                user.agent1CommissionBalance,
                                user.agent2CommissionBalance,
                                user.agent3CommissionBalance,
                                user.topAdminId,
                                user.topAdminUsername,
                                user.topAdminNickName,
                                user.topAdminCommission,
                                user.agent1Id,
                                user.agent1Username,
                                user.agent1NickName,
                                user.agent1Commission,
                                user.agent2Id,
                                user.agent2Username,
                                user.agent2NickName,
                                user.agent2Commission,
                                user.agent3Id,
                                user.agent3Username,
                                user.agent3NickName,
                                user.agent3Commission,
                                user.createAt
                        )
                );
    }
}
