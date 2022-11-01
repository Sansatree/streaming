package com.sstree.streaming.hlsconverter.ffmpeg;

import com.sstree.streaming.hlsconverter.config.FilePathConfig;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class ConverterHls {

    FilePathConfig filePathConfig = new FilePathConfig();

    FFmpeg fFmpeg = new FFmpeg( filePathConfig.getFfmpeg()+ "ffmpeg");
    FFprobe fFprobe = new FFprobe(filePathConfig.getFfmpeg() + "ffprobe");
    public ConverterHls() throws IOException {

    }

    /**
     * flv -> hls 트랜스코딩
     * audio bitrate 160k로 고정
     */

    public void hlsStream(){
        FFmpegBuilder fFmpegBuilder = new FFmpegBuilder()
                .setInput(filePathConfig.getVideo() + "testvideo-1650973569.flv")
                .addOutput(filePathConfig.getOutput() + "testvideo%v.m3u8")


                //720p
                .addExtraArgs("-s", "1280x720")
                .addExtraArgs("-b:v:0", "4000k")
                .addExtraArgs("-maxrate:v:0", "5000k")

                //480p
                .addExtraArgs("-s", "854x480")
                .addExtraArgs("-b:v:1", "2000k")
                .addExtraArgs("-maxrate:v:1", "2500k")

                //360p
                .addExtraArgs("-s", "640x360")
                .addExtraArgs("-b:v:2", "1000k")
                .addExtraArgs("-maxrate:v:2", "1000k")


                .addExtraArgs("-b:a:0","160k")
                .addExtraArgs("-b:a:1","150k")
                .addExtraArgs("-b:a:2","140k")

                .addExtraArgs("-map","0:v")
                .addExtraArgs("-map","0:a")
                .addExtraArgs("-map","0:v")
                .addExtraArgs("-map","0:a")
                .addExtraArgs("-map","0:v")
                .addExtraArgs("-map","0:a")

                .addExtraArgs("-f", "hls") //출력 형식 hls
                .addExtraArgs("-var_stream_map","v:0,a:0 v:1,a:1 v:2,a:2")

                .addExtraArgs("-hls_time","3") //ts 파일 길이 3초
                .addExtraArgs("-hls_list_size","0") //.m3u8에 포함할 .ts 파일 리스트의 길이를 설정 (0은 모두 저장)
                .addExtraArgs("-hls_segment_filename", "vs%v-testvideo%3d.ts") //ts파일 시작 번호
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(fFmpeg,fFprobe);
        executor.createJob(fFmpegBuilder).run();


    }
}