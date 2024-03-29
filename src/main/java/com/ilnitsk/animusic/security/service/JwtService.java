package com.ilnitsk.animusic.security.service;

import com.ilnitsk.animusic.security.CookieUtils;
import com.ilnitsk.animusic.user.dao.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JwtService {

    private final String secret_key;
    @Value("${token.expirationMinutes}")
    private long TTL_MINUTES;
    @Value("${token.refreshExpirationHours}")
    private long REFRESH_TTL_HOURS;

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private CookieUtils cookieUtils;

    @Autowired
    public JwtService(@Value("${token.secret}") String secret,CookieUtils cookieUtils){
        this.secret_key = secret;
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
        this.cookieUtils = cookieUtils;
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        ZonedDateTime tokenCreateTime = ZonedDateTime.now();
        Date tokenValidity = Date.from(tokenCreateTime.plusMinutes(TTL_MINUTES).toInstant());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    public ResponseCookie generateJwtCookie(User user) {
        String jwt = createToken(user);
        return cookieUtils.generateCookie("access-token",jwt,"/api");
    }


    public String parseUsernameFromJwt(String token) {
        return parseJwtClaims(token).getSubject();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            log.warn("Claims is invalid: {}",claims.getSubject());
            return false;
        }
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }
}
