package com.sstree.streaming.streamingserver.service.dto;

import com.sstree.streaming.streamingserver.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String username;

    public static UserResponseDto of(Users users) {
        return new UserResponseDto(users.getUsername());
    }
}
