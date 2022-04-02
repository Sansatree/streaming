package com.sstree.streaming.streamingserver.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class FilePathConfig {
    String ffmpegPath = "/bin/ffmpeg"; //ffmpeg 경로
    String ffprobePath = "/bin/ffprobe"; //ffprobe 경로
    String ffmpegTestPath = "C:\\ffmpeg\\bin\\ffmpeg";
    String ffprobeTestPath = "C:\\ffmpeg\\bin\\ffprobe";
    String flvFilePath = "/var/www/html/dwstreams/"; //전송받은 flv 파일 위치
    String flvFileName = "";

    String playListPath = ""; // m3u8 저장될 경로
    String playListName = ""; // m3u8 이름


}
