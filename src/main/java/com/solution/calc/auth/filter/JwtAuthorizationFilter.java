package com.solution.calc.auth.filter;


import com.solution.calc.auth.jwt.JwtTokenizer;
import com.solution.calc.auth.util.CustomAuthorityUtils;
import com.solution.calc.constant.Authority;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.exception.ServiceLogicException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenizer jwtTokenizer;

    private final CustomAuthorityUtils authorityUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            Map<String, Object> claims = getHeaderClaims(request);
            setAuthenticationToContext(claims);
            RequestAttributes requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
            requestContext.setAttribute("userId", claims.get("userId"), RequestAttributes.SCOPE_REQUEST);
        } catch (ExpiredJwtException ee) {
            request.setAttribute(
                    "exception",
                    new ServiceLogicException(ErrorCode.EXPIRED_ACCESS_TOKEN));
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request
    ) throws ServletException {
        String authorization = request.getHeader("Authorization");

        return authorization == null || !authorization.startsWith("Bearer ");
    }

    private Map<String, Object> getHeaderClaims(HttpServletRequest request) {
        String jwt = "";
        String findHeader = request.getHeader("Authorization").replace("Bearer ", "");
        if (findHeader == null || findHeader.isEmpty()) {
            throw new ServiceLogicException(ErrorCode.ACCESS_DENIED_REQUEST_API);
        } else {
            jwt = findHeader.replace("Bearer ", "");
        }
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        log.debug("### Success Find AccessToken In Header [API Request]");
        return jwtTokenizer.getClaims(jwt, base64EncodedSecretKey).getBody();
    }

    private void setAuthenticationToContext(Map<String, Object> claims) {
        String username = (String) claims.get("username");
        List<String> rolesList = (List<String>) claims.get("roles");
        if (username.equals("admin")) {
            rolesList = Authority.ADMIN.getStringRole();
        }
        List<GrantedAuthority> roles = authorityUtils.createAuthorities(rolesList);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null, roles);
        log.debug("### SecurityContextHolder SetAuthentication = {}", rolesList);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}