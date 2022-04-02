package com.sstree.streaming.streamingserver.controller;

import com.sstree.streaming.streamingserver.jwt.JwtTokenProvider;
import com.sstree.streaming.streamingserver.service.UserDetailServiceImpl;
import com.sstree.streaming.streamingserver.service.UserService;
import com.sstree.streaming.streamingserver.service.dto.TokenDto;
import com.sstree.streaming.streamingserver.service.dto.UserDto;
import com.sstree.streaming.streamingserver.service.dto.request.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserSaveController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final UserService userService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE )
    public Long signUp(@RequestBody UserDto userDto){
        userDetailServiceImpl.saveUser(userDto);
        return userDto.getId();
    }

    @PostMapping(value = "/login-page")
    public ResponseEntity login(@RequestBody LoginDto loginDto){

        return userService.login(loginDto);
    }

}
