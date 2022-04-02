package com.sstree.streaming.streamingserver.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;

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
        String requestURI = request.getRequestURI();

        // validationToken 으로 유효성 검사
        // 정상 토큰이면 해당 토큰으로  Authentication 을 가져와서 SecurityContext 에 저장
        if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)){
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Contetx에 '{}' 인증 정보를 저장했습니다. uri :{}",authentication.getName(),requestURI );
        }else{
            log.info("유요한 JWT 토큰이 없습니다. uri : {}",requestURI);
        }
            filterChain.doFilter(request,response);
    }

    //Request Header 에서 토큰 정보 꺼내오기
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }
}
