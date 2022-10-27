package com.sstree.streaming.streamingserver.controller;


import com.sstree.streaming.streamingserver.service.UserService;
import com.sstree.streaming.streamingserver.service.dto.TokenDto;
import com.sstree.streaming.streamingserver.service.dto.TokenRequestDto;
import com.sstree.streaming.streamingserver.service.dto.UserRequestDto;
import com.sstree.streaming.streamingserver.service.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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

    @PostMapping(value = "/login" )
    public TokenDto login(@RequestBody UserRequestDto userRequestDto) throws IOException {
        log.info("로그인 컨트롤러 동작");
        return null;
    }
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(HttpServletResponse response, @RequestBody TokenRequestDto tokenRequestDto) throws IOException {
        log.info("리이슈 컨트롤러 동작");
//        return ResponseEntity.ok(jwtTokenProvider.reissue(response,tokenRequestDto));
        return null;
    }

}
