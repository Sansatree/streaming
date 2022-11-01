package com.sstree.streaming.streamingserver.controller;


import com.sstree.streaming.streamingserver.service.UserService;
import com.sstree.streaming.streamingserver.service.dto.UserRequestDto;
import com.sstree.streaming.streamingserver.service.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<UserResponseDto> singUp(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok(userService.saveUser(userRequestDto));
    }

}
