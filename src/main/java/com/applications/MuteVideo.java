package com.applications;
import com.annotations.*;
import com.batcheador.OutputFile;

@Application(name="Mutear Video")
@Command(name="ffmpeg", route="/home/gonz4/ffmpeg")
public class MuteVideo {
	//ffmpeg -i video.mp4 -c copy -an muteado.mp4
	@Parameter(type="file", flags="-i", label="Archivo a mutear")
    private String file;

    @Parameter(type="outputFile", flags="-an", label="Nombre del archivo de salida")
    private OutputFile outputFile;
}
