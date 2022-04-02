package com.sstree.streaming.streamingserver.service.dto;

import com.sstree.streaming.streamingserver.entity.Role;
import com.sstree.streaming.streamingserver.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {

    private Long id;
    private String userId;
    private String email;
    private String password;
    private Role role;

    @Builder
    public UserDto(String userId, String email, String password, Role role) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = getRole();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User toEntity(){
        return User.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .role(role.USER)
                .build();

    }
}
