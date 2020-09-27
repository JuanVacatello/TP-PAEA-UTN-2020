package com.batcheador;

import java.util.ArrayList;
import java.util.List;

import com.applications.MuteVideo;

public class Main {
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
    	List<Class> applications = new ArrayList<Class>();
    	applications.add(MuteVideo.class);
    	
    	Batcheador batcheador = new Batcheador(applications);
    	batcheador.createWindow();
    }
}
