package com.user.applications;
import com.batcheador.annotations.*;

@Application(name="Audio a Video")
public class AudioToVideo {
	//ffmpeg -loop 1 -i img.png -i audio.mp3 -c:a codecAudio -c:v codecVideo -shortest salida.mp4
    @Parameter(type="file", flags="-i", label="Imagen para el video")
    private String image;

    @Parameter(type="file", flags="-i", label="Audio para el video")
    private String audio;

    @Parameter(type="text", flags="-shortest", label="Nombre del archivo de salida")
    private String outputFile;

    @Parameter(type="audioCodec", label="Codec de audio")
    private String audioCodec;

    @Parameter(type="videoCodec", label="Codec de video")
    private String videoCodec;

}
