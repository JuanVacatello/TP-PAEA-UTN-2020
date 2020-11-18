package com.user.applications;
import com.batcheador.OutputFile;
import com.batcheador.annotations.*;
import com.batcheador.utils.ParameterType;

@Application(name="Audio a Video")
public class AudioToVideo {
	//ffmpeg -loop 1 -i img.png -i audio.mp3 -c:a codecAudio -c:v codecVideo -shortest salida.mp4
    @Parameter(type= ParameterType.FILE, prefix="-loop 1", flags="-i", label="Imagen para el video")
    private String image;

    @Parameter(type=ParameterType.FILE, flags="-i", label="Audio para el video")
    private String audio;

    @Parameter(type=ParameterType.OUTPUTFILE, flags="-shortest", label="Nombre del archivo de salida")
    private OutputFile outputFile;

    @Parameter(type=ParameterType.AUDIOCODEC, flags="-c:a", label="Codec de audio")
    private String audioCodec;

    @Parameter(type=ParameterType.VIDEOCODEC, flags="-c:v", label="Codec de video")
    private String videoCodec;

}
