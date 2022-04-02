package com.sstree.streaming.streamingserver.service.dto;

import com.sstree.streaming.streamingserver.entity.Role;
import com.sstree.streaming.streamingserver.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String password;
    private List<String> roles = new ArrayList<>();

    @Builder
    public UserDto(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = getRoles();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User toEntity(){
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .roles(roles)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(username,password);
    }

//    @Getter
//    public static class Login{
//        private String userId;
//        private String password;
//
//        public UsernamePasswordAuthenticationToken toAuthentication(){
//            return new UsernamePasswordAuthenticationToken(userId,password);
//        }
//    }


}
