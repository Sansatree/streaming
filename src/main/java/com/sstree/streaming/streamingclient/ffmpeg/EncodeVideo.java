package com.sstree.streaming.streamingclient.ffmpeg;

import com.sstree.streaming.streamingclient.config.FilePathConfig;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EncodeVideo {

    FilePathConfig pathConfig = new FilePathConfig();
    FFmpeg fFmpeg = new FFmpeg(pathConfig.getFfmpegPath() + "ffmpeg");
    FFprobe fFprobe = new FFprobe(pathConfig.getFfmpegPath() + "ffprobe");
    FFmpegExecutor executor = new FFmpegExecutor(fFmpeg,fFprobe);

    public EncodeVideo() throws IOException {
    }


    /**
     * 로컬카메라로 녹화
     * encode h264,aac
     * output nginx로 flv
     * output 로컬로 .mp4
     */
    public void videoEncode(){
        FFmpegBuilder builder = new FFmpegBuilder()
                .addExtraArgs("-rtbufsize", "15M")// 버퍼사이즈 수정
                .addExtraArgs("-re")
                .setFormat("dshow")
                .addInput("video=\"WebCam\":audio=\"Microphone\"")
                .addOutput(pathConfig.getOutputMp4Path() + "\\" + pathConfig.getVideoName() + ".mp4")
                .setVideoCodec("libx264") // x264는 cpu 사용, h.264는 Gpu 사용
                .addExtraArgs("-bufsize", "5000k")  //버퍼사이즈
                .addExtraArgs("-minrate", "1000k") //
                .addExtraArgs("-maxrate", "5000k")
                .setAudioCodec("aac") // 오디오 코덱 aac
                .setAudioSampleRate(FFmpeg.AUDIO_SAMPLE_44100) //오디오 표준 sample rate 값(44.1khz)
                .setAudioBitRate(1_600_000)  // 오디오 비트레이트 128K로 수정
                .addExtraArgs("-profile:v", "baseline")
                .setVideoPixelFormat("yuv420p")
                .setVideoResolution(1280, 720) // videosize (-s)
                .setVideoBitRate(2_000_000)
                .setVideoFrameRate(60)  // 내 노트북은 30프레임까지만 가능 video frame rate (-r)
                .addExtraArgs("-preset", "medium")
                .addExtraArgs("-f","flv")
                .addExtraArgs(pathConfig.getRtmpPath() + "/" + pathConfig.getVideoName())
                .done();
        executor.createJob(builder).run();

    }
}
