package com.solution.calc.auth.jwt;


import com.solution.calc.auth.token.Token;
import com.solution.calc.constant.ErrorCode;
import com.solution.calc.domain.user.entity.User;
import com.solution.calc.exception.ServiceLogicException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenizer {

    @Setter
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    private final int accessTokenExpirationMinutes = 50000;

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /* jwt 토큰을 생성 */
    private Token generateToken(
            Map<String, Object> claims,
            String subject,
            String base64EncodedSecretKey
    ) {
        Key key = getKeyFromBase64EncodedSecretKey(base64EncodedSecretKey);

        return new Token(
                "Bearer " + Jwts.builder()
                        .setClaims(claims)
                        .setSubject(subject)
                        .setIssuedAt(Calendar.getInstance().getTime())
                        .setExpiration(getTokenExpiration(accessTokenExpirationMinutes))
                        .signWith(key)
                        .compact());

    }

    /* user 매개변수를 받아 jwt 토큰을 생성 */

    public Token delegateToken(
            User user
    ) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("roles", user.getRoles());
        claims.put("userLevel", user.getUserLevel().getLevel());
        String subject = user.getUsername();
        String base64SecretKey = encodeBase64SecretKey(getSecretKey());
        return generateToken(claims, subject, base64SecretKey);
    }


    /* AccessToken 검증 */
    public void verifyAccessToken(
            String accessToken
    ) {
        String base64SecretKey = encodeBase64SecretKey(getSecretKey());
        try {
            verifySignature(getJws(accessToken), base64SecretKey);
        } catch (ExpiredJwtException ee) {
            throw new ServiceLogicException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw e;
        }
    }

    /* Server에서 발급한 토큰이 맞는지 검증 */
    private void verifySignature(String jwt, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedSecretKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt);
    }

    /* Claims 정보를 가져옴 */
    public Jws<Claims> getClaims(String jwt, String base64EncodedSecretKey) {
        verifyAccessToken(jwt);
        Key key = getKeyFromBase64EncodedSecretKey(base64EncodedSecretKey);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt);
    }

    /* Token의 만료 기한 설정 */
    private Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        return calendar.getTime();
    }

    /* Token에서 UserId 정보를 가져옴 */
    public String getUsername(String token) {
        Key key = getKeyFromBase64EncodedSecretKey(encodeBase64SecretKey(secretKey));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(getJws(token))
                .getBody()
                .getSubject()
                ;
    }


    /* Secret key 생성 */
    private Key getKeyFromBase64EncodedSecretKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String getJws(String accessToken) {
        return accessToken.replace("Bearer ", "");
    }

}