package com.applications;
import com.annotations.*;

@Application(name="Audio a Video")
@Command(name="ffmpeg.exe", route="/home/gonz4/ffmpeg")

public class AudioToVideo {
	//ffmpeg -loop 1 -i img.png -i audio.mp3 -c:a codecAudio -c:v codecVideo -shortest salida.mp4
	@Parameter(type="file", flags="-i", label="Imagen para el video")
    private String image;
	
	@Parameter(type="file", flags="-i", label="Audio para el video")
    private String audio;
	
    @Parameter(type="file", flags="-shortest", label="Nombre del archivo de salida")
    private String outputFile;

}


