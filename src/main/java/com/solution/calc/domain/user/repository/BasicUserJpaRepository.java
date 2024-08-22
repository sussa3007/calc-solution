package com.solution.calc.domain.user.repository;

import com.solution.calc.domain.user.entity.BasicUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasicUserJpaRepository extends JpaRepository<BasicUser, Long> {

    Page<BasicUser> findAllByOfficeId(Long officeId, Pageable pageable);

    Optional<BasicUser> findByUsernameAndOfficeId(String username, Long officeId);
}
