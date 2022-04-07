package com.sstree.streaming.streamingserver.service;

import com.sstree.streaming.streamingserver.entity.RefreshToken;

import com.sstree.streaming.streamingserver.entity.Users;
import com.sstree.streaming.streamingserver.jwt.JwtTokenProvider;
import com.sstree.streaming.streamingserver.jwt.SecurityUtil;
import com.sstree.streaming.streamingserver.repository.RefreshTokenRepository;
import com.sstree.streaming.streamingserver.repository.UserRepository;
import com.sstree.streaming.streamingserver.service.dto.TokenDto;
import com.sstree.streaming.streamingserver.service.dto.TokenRequestDto;
import com.sstree.streaming.streamingserver.service.dto.UserRequestDto;
import com.sstree.streaming.streamingserver.service.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailServiceImpl userDetailService;

    @Transactional
    public UserResponseDto saveUser(UserRequestDto userRequestDto){

        if (userRepository.existsByUsername(userRequestDto.getUsername())){
            throw new RuntimeException("이미 가입한 아이디입니다.");
        }
        Users users = userRequestDto.toEntity(passwordEncoder);
        return UserResponseDto.of(userRepository.save(users));

    }
    @Transactional
    public TokenDto login(UserRequestDto userRequestDto){
        if(userRepository.findByUsername(userRequestDto.getUsername()).orElse(null)==null){
           log.info("해당 아이디가 없습니다 {}", userRequestDto.getUsername());
        }
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
//        log.info("++++++++++UserService Login+++++++++++++++++++++");
//        UsernamePasswordAuthenticationToken authenticationToken = userRequestDto.toAuthentication();
//        log.info("++++++++++UserService usernamepasswrodToken+++++++++++++++++++++");
//        log.info("usernamepasswrodToken {}" , authenticationToken);
//        log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//        log.info("usernamepasswordauthenticationtoken : {}" ,authenticationToken);
//        log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        SecurityContextHolder.getContext().setAuthentication(authentication);

//        log.info("authentication {}", authentication);

        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);
//

        log.info("accesstoken이 생성되었습니다. ");

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        String jwt = tokenDto.getAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);

//        return new ResponseEntity(tokenDto.getAccessToken(), httpHeaders,HttpStatus.OK);

        //토큰 발급
        return tokenDto;
//        return response.success(tokenDto.getAccessToken(), "로그인 성공" , HttpStatus.OK);

    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    @Transactional(readOnly = true)
    public UserResponseDto getMemberInfo(String username) {
        return userRepository.findByUsername(username)
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public UserResponseDto getMyInfo() {
        return userRepository.findById(SecurityUtil.getCurrentUserId())
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }


}
