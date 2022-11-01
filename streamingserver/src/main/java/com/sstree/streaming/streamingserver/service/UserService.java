package com.sstree.streaming.streamingserver.service;

import com.sstree.streaming.streamingserver.entity.Users;
import com.sstree.streaming.streamingserver.repository.UserRepository;
import com.sstree.streaming.streamingserver.service.dto.UserRequestDto;
import com.sstree.streaming.streamingserver.service.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto saveUser(UserRequestDto userRequestDto){

        if (userRepository.existsByUsername(userRequestDto.getUsername())){
            throw new RuntimeException("이미 가입한 아이디입니다.");
        }
        Users users = userRequestDto.toEntity(passwordEncoder);
        return UserResponseDto.of(userRepository.save(users));
    }
}
