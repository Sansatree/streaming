package com.sstree.streaming.streamingserver.controller;

import com.sstree.streaming.streamingserver.service.dto.UserDto;
import com.sstree.streaming.streamingserver.service.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class UserController {

    private UserDetailServiceImpl userDetailServiceImpl;

    @GetMapping()
    public String home(){
        return "";
    }

    @GetMapping("/login-page")
    public String login() {

        return "login-page";
    }

    @GetMapping("/signup")
    public String signup(UserDto userDto){

        return "signup";
    }


}
