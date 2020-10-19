package com.batcheador;

import java.util.ArrayList;
import java.util.List;

import com.applications.AudioToVideo;
import com.applications.CutVideo;
import com.applications.MuteVideo;

public class Main {
    public static void main(String[] args) throws Exception {
    	List<Class> applications = new ArrayList<Class>();

		applications.add(AudioToVideo.class);
    	applications.add(CutVideo.class);
    	applications.add(MuteVideo.class);

    	Batcheador batcheador = new Batcheador(applications);
    	batcheador.createWindow();
    }
}
