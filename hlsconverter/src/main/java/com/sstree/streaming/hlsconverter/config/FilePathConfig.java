package com.sstree.streaming.hlsconverter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class FilePathConfig {

    private static String ffmpeg;
    private static String video;
    private static String output;

    public String getFfmpeg() {
        return ffmpeg;
    }
    
    //ffmpeg 주소
    @Value("${path.ffmpeg}")
    public void setFfmpeg(String ffmpeg) {
        this.ffmpeg = ffmpeg;
    }

    public String getVideo() {
        return video;
    }
    
    // video 저장 위치
    @Value("${path.video}")
    public void setVideo(String video) {
        this.video = video;
    }

    public String getOutput() {
        return output;
    }
    
    // .ts 파일 저장될 위치
    @Value("${path.output}")
    public void setOutput(String output) {
        this.output = output;
    }
}
