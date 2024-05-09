package com.ssafy.user.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

//    @Value("${jwt.secret}")
//    private String secretKey;
    public String secretKey = "ssafy1234"; // 환경 변수로 관리하는 것이 좋습니다.

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey.getBytes())
            .parseClaimsJws(token)
            .getBody();
        return Long.parseLong(claims.getSubject()); // `subject`를 사용자 ID로 사용
    }


    public String generateToken(Long userId) {
        return Jwts.builder()
            .setSubject(String.valueOf(userId)) // 사용자 ID를 문자열로 변환
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10시간 후 만료
            .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
            .compact();
    }


    public boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }

    private Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }
}

