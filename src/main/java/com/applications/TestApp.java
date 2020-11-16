package com.applications;

import com.annotations.Application;
import com.annotations.Command;
import com.annotations.Parameter;

@Application(name="Testear batcheador")
@Command(name="ffmpeg", route="/home/gonz4/ffmpeg")
public class TestApp {
	//ffmpeg -i video.mp4 -c copy -an muteado.mp4
	@Parameter(type="file", flags="-i", label="File")
    private String file;

    @Parameter(type="text", flags="-t", label="Text")
    private String text;

    @Parameter(type="number", label="Number")
    private int number;

    @Parameter(type="audioCodec", label="Audio codec")
    private String audioCodec;

    @Parameter(type="videoCodec", label="Video codec")
    private String videoCodec;
}