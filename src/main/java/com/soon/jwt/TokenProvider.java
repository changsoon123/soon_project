package com.soon.jwt;

import com.soon.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     * JWS 생성 메서드
     *
     *
     */
    public String createToken(User userEntity) {

        // 토큰 만료시간 4시간으로 설정
        Date expiry = Date.from(
                Instant.now().plus(4, ChronoUnit.HOURS)
        );

        Map<String, Object> claims = new HashMap<>();
        claims.put("name", userEntity.getUsername());


        return Jwts.builder()
                .signWith(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),
                        SignatureAlgorithm.HS512
                )
                .setClaims(claims)
                .setIssuer("Soon")
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .setSubject(userEntity.getUsername())
                .compact();
    }

    /**
     * 클라이언트에서 보낸 토큰값 검증 후 토큰 정보 리턴
     * @param token - 클라이언트에게 받은 토큰
     * @return - 토큰 안에 있는 인증된 유저 정보 리턴
     */
    public TokenUserInfo validateAndReturnTokenUserInfo(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        log.info("claims : {}", claims);

        return TokenUserInfo.builder()
                .userId(claims.getSubject())
                .userNick(claims.get("customClaim", String.class))
                .build();

    }

}
