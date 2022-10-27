package com.sstree.streaming.streamingserver.jwt;

import com.sstree.streaming.streamingserver.entity.RefreshToken;
import com.sstree.streaming.streamingserver.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static com.sstree.streaming.streamingserver.jwt.JwtTokenProvider.*;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer";
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
    // 인증에서 제외할 url
    private static final List<String> EXCLUDE_URL =
            Collections.unmodifiableList(
                    Arrays.asList(

                    ));

     */

    /**
     * 필터링 로직 수행
     * jwt 토큰 인증 정보를 현재 쓰레드의 securitycontext 에 저장
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("oncePerRequestFilter 동작");

        // validationToken 으로 유효성 검사
        // 정상 토큰이면 해당 토큰으로  Authentication 을 가져와서 SecurityContext 에 저장
            if (request.getServletPath().equals("/auth/login")|| request.getServletPath().equals("/livestream/hls") ) {
                String jwt = resolveToken(request);
                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) == JwtCode.ACCESS) {
                    log.info("인증 필터 동작");
                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                else if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) == JwtCode.EXPIRED){
                    log.info("만료 필터 동작");
                    Authentication authentication = tokenProvider.getAuthentication(jwt);

                    RefreshToken findRefreshToken = refreshTokenRepository.findByKey(authentication.getName())
                            .orElseThrow(() -> new UsernameNotFoundException("refreshToken key : " + authentication.getName() + "이 일치하지 않습니다."));
                    String refreshToken = findRefreshToken.getValue();
                    tokenProvider.reissue(response,jwt,refreshToken);

                }

            }
            filterChain.doFilter(request, response);
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


/**
 * OncePerRequestFilter 는 요청마다 실행됨
 새로고침시 Token 유효성 검사
 만약 토큰 만료시 expiredjwtexception로 /livestream/hls 접근 불가

 해결
 1. exception에대한 handler로 처리
    - /auth/reeissue 예외처리
 */