package com.sstree.streaming.streamingserver.service;

import com.sstree.streaming.streamingserver.entity.RefreshToken;
import com.sstree.streaming.streamingserver.jwt.JwtTokenProvider;
import com.sstree.streaming.streamingserver.repository.RefreshTokenRepository;
import com.sstree.streaming.streamingserver.repository.UserRepository;
import com.sstree.streaming.streamingserver.service.dto.TokenDto;
import com.sstree.streaming.streamingserver.service.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        loginError(new UserRequestDto());
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        String jwt = tokenDto.getAccessToken();

        Cookie cookie = new Cookie("token", "Bearer" + jwt);
        cookie.setPath("/   ");
        cookie.setMaxAge(10 * 60); // cookie 유지시간 초단위
        response.addCookie(cookie);


        response.sendRedirect("/livestream/hls");

    }

    public void loginError(UserRequestDto userRequestDto){

        if(userRepository.findByUsername(userRequestDto.getUsername()).orElse(null)==null){
            log.info("해당 아이디가 없습니다 {}", userRequestDto.getUsername());
        }
    }



}
