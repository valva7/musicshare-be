package org.musicshare.domain.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.member.model.entity.MemberEntity;
import org.musicshare.domain.member.repository.JpaMemberRepository;
import org.musicshare.global.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenProvider {

    private final SecretKey key;
    private static final long TOKEN_VALID_TIME = 1000L * 60 * 60; // 1시간
    private static long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 1440; // 1 day

    private final JpaMemberRepository jpaMemberRepository;

    public TokenProvider(@Value("${secret-key}") String secretKey, JpaMemberRepository jpaMemberRepository) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jpaMemberRepository = jpaMemberRepository;
    }

    // 최초 액세스 토큰 생성
    public String createAccessToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALID_TIME);

        // Claims 객체 생성 및 값 설정
        Claims claims = Jwts.claims();
        claims.put("nickname", new String(member.getInfo().getNickname().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(member.getId()))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // 리프레시 토큰을 통해 새로운 액세스 토큰 생성
    public String createNewAccessToken(String refreshToken) {
        Long userId = getUserId(refreshToken);
        MemberEntity memberEntity = jpaMemberRepository.findById(userId).orElseThrow(MemberNotFoundException::new);
        return createAccessToken(memberEntity.toMember());
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

        // Claims 객체 생성 및 값 설정
        Claims claims = Jwts.claims()
            .setSubject(userId.toString());

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, key)
            .compact();
    }

    public Long getUserId(String token) {
        return Long.parseLong(
            Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject()
        );
    }

    // 🔹 JWT 검증 (서명, 만료시간, 발급자 등 체크)
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token.substring(7)) // Bearer 제거
                .getBody();
            return true;

        } catch (ExpiredJwtException e) {
            log.info("유효하지 않은 토큰: 만료된 토큰입니다.");
        } catch (MalformedJwtException e) {
            log.info("유효하지 않은 토큰: 올바르지 않은 형식의 JWT입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("유효하지 않은 토큰: 지원되지 않는 JWT 형식입니다.");
        } catch (SignatureException e) {
            log.info("유효하지 않은 토큰: 서명이 유효하지 않습니다.");
        } catch (JwtException e) {
            log.info("유효하지 않은 토큰: JWT 관련 예외 발생 - " + e.getMessage());
        } catch (Exception e) {
            log.info("유효하지 않은 토큰: 기타 예외 발생 - " + e.getMessage());
        }
        return false;
    }

}