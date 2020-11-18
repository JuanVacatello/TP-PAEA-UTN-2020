package com.user.applications;
import com.batcheador.annotations.*;

@Application(name="Cortar Video")
public class CutVideo {
	//ffmpeg -i video.mp4 -ss segundosInicio -t segundosFin cut.mp4
    @Parameter(type="file", flags="-i", label="Archivo a cortar")
    private String file;

    @Parameter(type="number", flags="-ss", label="Tiempo de inicio en segundos")
    private int startTime;
    
    @Parameter(type="number", flags="-t", label="Tiempo de fin en segundos")
    private int endTime;

    @Parameter(type="text", label="Nombre del archivo de salida")
    private String outputFile;
}
