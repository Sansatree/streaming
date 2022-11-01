package com.sstree.streaming.streamingserver.service.dto;

import com.sstree.streaming.streamingserver.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.sstree.streaming.streamingserver.entity.Role.ROLE_USER;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private String username;
    private String password;

    public Users toEntity(PasswordEncoder passwordEncoder){
        return com.sstree.streaming.streamingserver.entity.Users.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(ROLE_USER)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(username,password);
    }
}
