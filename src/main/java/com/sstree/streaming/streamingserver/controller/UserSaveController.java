package com.sstree.streaming.streamingserver.controller;

import com.sstree.streaming.streamingserver.entity.User;
import com.sstree.streaming.streamingserver.service.UserDetailServiceImpl;
import com.sstree.streaming.streamingserver.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserSaveController {
    private final UserDetailServiceImpl userDetailServiceImpl;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE )
    public Long signUp(@RequestBody UserDto userDto){
        userDetailServiceImpl.saveUser(userDto);
        return userDto.getId();
    }

//    @PostMapping(value = "/login-page")
//    public String login(UserDto userDto){
//
//        return ;
//    }
}
