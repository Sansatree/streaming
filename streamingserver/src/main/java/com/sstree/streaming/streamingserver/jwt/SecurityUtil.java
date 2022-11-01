package com.sstree.streaming.streamingserver.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    //SecurityContext 에 유저 정보 저장
    //Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    //SecurityContext 는 ThreadLocal 에 사용자의 정보를 저장합니다.
    public static Long getCurrentUserId(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null){
            throw new RuntimeException("Context에 인증 정보가 없습니다.");
        }
        return Long.parseLong(authentication.getName());
    }
}
