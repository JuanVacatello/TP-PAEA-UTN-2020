package com.batcheador;

import java.util.ArrayList;
import java.util.List;

import com.applications.*;

public class Main {
    public static void main(String[] args) {
    	List<Class> applications = new ArrayList<Class>();

		applications.add(AudioToVideo.class);
    	applications.add(CutVideo.class);
		applications.add(MuteVideo.class);
		applications.add(TestApp.class);

    	Batcheador batcheador = new Batcheador(applications);
    	batcheador.createWindow();
    }
}
