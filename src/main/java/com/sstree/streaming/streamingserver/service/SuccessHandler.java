package com.sstree.streaming.streamingserver.service;

import com.sstree.streaming.streamingserver.entity.RefreshToken;
import com.sstree.streaming.streamingserver.jwt.JwtTokenProvider;
import com.sstree.streaming.streamingserver.repository.RefreshTokenRepository;
import com.sstree.streaming.streamingserver.repository.UserRepository;
import com.sstree.streaming.streamingserver.service.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    public static final String BEARER_PREFIX = "Bearer";

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        loginError(new UserRequestDto());
        log.info("성공 핸들러 동작");
        log.info("request.getCookies = {}", request.getCookies());

        if (request.getCookies() == null)  {
            String accessToken =  jwtTokenProvider.createAccessToken(authentication);
            String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

            log.info("엑세스 토큰 생성 = {} ", accessToken);
            log.info("리프레시 토큰 생성 = {} ", refreshToken);
            RefreshToken refreshTokenBuild = RefreshToken.builder()
                    .key(authentication.getName())
                    .value(refreshToken)
                    .build();
            refreshTokenRepository.save(refreshTokenBuild);

            Cookie cookie = new Cookie("token", "Bearer" + accessToken);
            cookie.setPath("/");
            cookie.setMaxAge(10 * 180); // cookie 유지시간 초단위 3분

            response.addCookie(cookie);
            log.info("cookie value {} ", cookie.getValue());
        }

        response.sendRedirect("/livestream/hls");
    }

    public void loginError(UserRequestDto userRequestDto){

        if(userRepository.findByUsername(userRequestDto.getUsername()).orElse(null)==null){
            log.info("해당 아이디가 없습니다 {}", userRequestDto.getUsername());
        }
    }

    private String resolveToken(HttpServletRequest request){

        String bearerToken = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        log.info("bearerToken ====== {} ", bearerToken);
        log.info("bearerToken.startWith ====== {} ", bearerToken.startsWith(BEARER_PREFIX));
        log.info("Stringutil.hastext(bearerToken) ====== {}" , StringUtils.hasText(bearerToken) );
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(6);
        }
        return null;
    }

}
