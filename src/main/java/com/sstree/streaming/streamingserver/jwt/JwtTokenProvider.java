package com.sstree.streaming.streamingserver.jwt;

import com.sstree.streaming.streamingserver.entity.RefreshToken;
import com.sstree.streaming.streamingserver.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JwtTokenProvider {

    private final RefreshTokenRepository refreshTokenRepository;
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 1 * 1000; // 토큰 유효기간 30초
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 60 * 60 * 24 * 1 * 1000; // 토큰 유효기간 1일
    private final Key key;

    //객체 초기화, secretKey Base64로 인코딩
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 생성
    public String createAccessToken(Authentication authentication) {
        //권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        log.info("엑세스 토큰 생성 authorities = {}", authorities);
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        return Jwts.builder()
                .setSubject(authentication.getName()) // token 제목 (Header)
                .claim(AUTHORITIES_KEY, authorities) // payload "auth" : "ROLE_USER"
                .setExpiration(accessTokenExpiresIn) // accessToken 유효시간
                .signWith(key, SignatureAlgorithm.HS512) //signature 암호화 알고리즘
                .compact();

    }

    //Refresh Token 생성
    public String createRefreshToken(Authentication authentication) {
        //권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        return Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME)) // refreshToken 유효시간
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512) // key 암호화 알고리즘
                .compact();

    }

    @Transactional
    public String reissue(HttpServletResponse response, String accessToken, String refreshToken) throws RuntimeException {
        // 1. Access Token 에서 Member ID 가져오기
        Authentication authentication = getAuthentication(accessToken);
        // 2. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken findRefreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("refreshToken key : " + authentication.getName() + "이 일치하지 않습니다."));

        log.info("get.value refreshToken = {} ", findRefreshToken.getValue());
        log.info("refreshToken = {}", refreshToken);
        if (findRefreshToken.getValue().equals(refreshToken)) {
            String createAccessToken = createAccessToken(authentication);
            Cookie cookie = new Cookie("token", "Bearer" + createAccessToken);
            cookie.setPath("/");
            cookie.setMaxAge(10 * 180); // cookie 유지시간 초단위

            response.addCookie(cookie);
            return createAccessToken;
        } else {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
    }


    //JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        //토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        // Claims에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    // 토큰 정보를 검증하는 메서드
    public JwtCode validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return JwtCode.ACCESS;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("만료된 JWT 토큰입니다.", e);
        } catch (ExpiredJwtException e) {
//            log.info("만료된 JWT 토큰입니다.", e);
            return JwtCode.EXPIRED;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.", e);
        }
        return JwtCode.DENIED;
    }

    // 복호화
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public static enum JwtCode {
        DENIED,
        ACCESS,
        EXPIRED;
    }
}
