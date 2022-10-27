package com.sstree.streaming.streamingserver.controller;

import com.sstree.streaming.streamingserver.service.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LiveStreamController {

    @GetMapping("/login")
    public ModelAndView login(UserRequestDto userRequestDto){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject(userRequestDto);
        return modelAndView;
    }
    @GetMapping("/signup")
    public ModelAndView signup(UserRequestDto userRequestDto){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signup");
        modelAndView.addObject(userRequestDto);
        return modelAndView;
    }
    @GetMapping("/livestream/hls")
    public ModelAndView videoHls(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("livestream/hls");
        return modelAndView;
    }

}
