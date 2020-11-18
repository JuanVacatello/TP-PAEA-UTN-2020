package com.user.applications;
import com.batcheador.OutputFile;
import com.batcheador.annotations.*;
import com.batcheador.utils.ParameterType;

@Application(name="Cortar Video")
public class CutVideo {
	//ffmpeg -i video.mp4 -ss segundosInicio -t segundosFin cut.mp4
    @Parameter(type= ParameterType.FILE, flags="-i", label="Archivo a cortar")
    private String file;

    @Parameter(type=ParameterType.NUMBER, flags="-ss", label="Tiempo de inicio en segundos")
    private int startTime;
    
    @Parameter(type=ParameterType.NUMBER, flags="-t", label="Tiempo de fin en segundos")
    private int endTime;

    @Parameter(type=ParameterType.OUTPUTFILE, label="Nombre del archivo de salida")
    private OutputFile outputFile;
}
