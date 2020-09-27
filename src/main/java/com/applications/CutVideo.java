package com.applications;
import com.annotations.*;

@Application(name="Cortar Video")
@Command(name="ffmpeg.exe", route="/home/gonz4/ffmpeg")

public class CutVideo {
	//ffmpeg -i video.mp4 -ss segundosInicio -t segundosFin cut.mp4
    @Parameter(type="file", flags="-i", label="Archivo a cortar")
    private String file;

    @Parameter(type="number", flags="-ss", label="Tiempo de inicio en segundos")
    private int startTime;
    
    @Parameter(type="number", flags="-t", label="Tiempo de fin en segundos")
    private int endTime;

    @Parameter(type="file", label="Nombre del archivo de salida")
    private String outputFile;
}