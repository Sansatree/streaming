package com.sstree.streaming.hlsconverter.ffmpeg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class Runner implements ApplicationRunner {
    @Autowired
    private ConverterHls converterHls;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        converterHls.hlsStream();
    }
}
