package com.solution.calc.auth.service;


import com.solution.calc.domain.user.entity.User;
import com.solution.calc.domain.user.service.UserService;
import com.solution.calc.exception.ServiceLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return null;
        } catch (ServiceLogicException e) {
            throw e;
        }
    }

    private final class UserDetail extends User implements UserDetails {

        public UserDetail(User user) {
            setUserId(user.getUserId());
            setUsername(user.getUsername());
            setPassword(user.getPassword());
            setUserLevel(user.getUserLevel());
            setUserStatus(user.getUserStatus());
            setRoles(user.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}