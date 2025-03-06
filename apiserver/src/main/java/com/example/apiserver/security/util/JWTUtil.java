package com.example.apiserver.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
// 스프링 환경이 아닌 곳에서 사용할 수 있도록 토큰을 발행할 수 있는 유틸리티
public class JWTUtil {
  private String secretKey = "1234567890abcdefghijklmnopqrstuvwxyz";
  private long expire = 60 * 24 * 30;

  // JWT 생성
  public String generateToken(String content) throws Exception {
    return Jwts.builder()
        .issuedAt(new Date())
        .expiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
        .claim("sub", content)
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
        .compact();
  }

  // JWT 검증 및 email축출
  public String validateAndExtract(String tokenStr) throws Exception {
    log.info("Jwts getClass; " +
        Jwts.parser().verifyWith(
                Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .build().parse(tokenStr));
    Claims claims = (Claims) Jwts.parser().verifyWith(Keys.hmacShaKeyFor(
        secretKey.getBytes(StandardCharsets.UTF_8))).build().parse(tokenStr).getPayload();
    return (String) claims.get("sub");
  }
}
