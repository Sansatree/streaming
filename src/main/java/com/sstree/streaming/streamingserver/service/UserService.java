package com.sstree.streaming.streamingserver.service;

import com.sstree.streaming.streamingserver.entity.RefreshToken;
import com.sstree.streaming.streamingserver.jwt.JwtTokenProvider;
import com.sstree.streaming.streamingserver.repository.RefreshTokenRepository;
import com.sstree.streaming.streamingserver.repository.UserRepository;
import com.sstree.streaming.streamingserver.service.dto.TokenDto;
import com.sstree.streaming.streamingserver.service.dto.request.LoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public ResponseEntity login(LoginDto loginDto){
        if(userRepository.findByUsername(loginDto.getUsername()).orElse(null)==null){
           log.info("해당 아이디가 없습니다 {}", loginDto.getUsername());
        }
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        log.info("accesstoken이 생성되었습니다. ");

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        //토큰 발급
        return ResponseEntity.ok(tokenDto);
    }

}
