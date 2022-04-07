package com.sstree.streaming.streamingserver.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer";
    private final JwtTokenProvider tokenProvider;

    /**
     * 필터링 로직 수행
     * JWT 토큰 인증 정보를 현재 쓰레드의 SecurityContext 에 저장
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Request Header 에서 AccessToken 꺼냄
            String jwt = resolveToken(request);

        // validationToken 으로 유효성 검사
        // 정상 토큰이면 해당 토큰으로  Authentication 을 가져와서 SecurityContext 에 저장
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            log.info("jwtFilter authentication = tokenprovicer.getauthentication(jwt) {}", authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
            filterChain.doFilter(request,response);


    }

    //Cookie 에서 토큰 정보 꺼내오기
    private String resolveToken(HttpServletRequest request){

        String bearerToken = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(6);
        }
        return null;
    }
}
