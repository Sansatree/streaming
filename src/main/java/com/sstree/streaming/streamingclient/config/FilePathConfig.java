package com.sstree.streaming.streamingclient.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.File;

@Getter
@Component
public class FilePathConfig {

    String separator = File.separator;

    String ffmpegPath = "C:\\ffmpeg\\bin\\";  //ffmpeg.exe, ffprobe.exe 경로
    String outputMp4Path = "C:\\streaming"; //ffmpeg 으로 만들 파일 경로
    String rtmpPath = "rtmp://sstree@172.30.85.109:1935/live"; //nginx 서버 주소
    String videoName = "testvideo"; //비디오 파일 이름

}
