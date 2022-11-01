package com.sstree.streaming.streamingclient;

import com.sstree.streaming.streamingclient.ffmpeg.EncodeVideo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class StreamingclientApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(StreamingclientApplication.class, args);
		EncodeVideo encodeVideo = new EncodeVideo();
		encodeVideo.videoEncode();
	}

}
