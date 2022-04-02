package com.sstree.streaming.streamingserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LiveStreamController {

    @GetMapping("/livestream/hls")
    public String videoHls(){

        return "livestream/hls";
    }
}
