package com.khanhdtk.QuanLyBanVeXemPhim.security;

import com.khanhdtk.QuanLyBanVeXemPhim.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    // Chìa khóa bí mật (Thực tế nên để trong application.properties, đây cháu hardcode cho bác dễ chạy)
    private static final String SECRET_KEY = "DayLaMotChieuKhoaBiMatCucKyDaiVaAnToanChoDuAnRapPhimCuaBacDayNhe";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Token sống trong 24 giờ
    private final long EXPIRATION_TIME = 86400000;

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
