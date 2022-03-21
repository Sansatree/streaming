package com.sstree.streaming.streamingclient.ffmpeg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class Runner implements ApplicationRunner {
    EncodeVideo encodeVideo = new EncodeVideo();

    public Runner() throws IOException {
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        encodeVideo.videoEncode();
    }
}
