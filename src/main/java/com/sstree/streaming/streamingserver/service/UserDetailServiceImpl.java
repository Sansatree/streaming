package com.sstree.streaming.streamingserver.service;

import com.sstree.streaming.streamingserver.service.dto.UserDto;
import com.sstree.streaming.streamingserver.entity.User;
import com.sstree.streaming.streamingserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
//    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + "아이디가 없습니다."));
    }


    private UserDetails createUserDetails(User user){
        return new User(user.getUsername(),user.getPassword(),user.getAuthorities());
    }

    @Transactional
    public Long saveUser(UserDto userDto){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if (userRepository.existsByUsername(userDto.getUsername())){
            throw new RuntimeException("이미 가입한 아이디입니다.");
        }
        return userRepository.save(userDto.toEntity()).getId();

    }



}
