package com.user.applications;
import com.batcheador.annotations.*;
import com.batcheador.OutputFile;
import com.batcheador.utils.ParameterType;

@Application(name="Mutear Video")
public class MuteVideo {
	//ffmpeg -i video.mp4 -c copy -an muteado.mp4
	@Parameter(type= ParameterType.FILE, flags="-i", label="Archivo a mutear")
    private String file;

    @Parameter(type=ParameterType.OUTPUTFILE, flags="-an", label="Nombre del archivo de salida")
    private OutputFile outputFile;
}
