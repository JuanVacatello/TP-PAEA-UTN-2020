package com.batcheador;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.annotations.*;

public class Batcheador {
	private List<Class> apps;
	
	public Batcheador(List<Class> applications) {
			this.apps = applications;
	}
	
    public void createWindow() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		JFrame frame = new JFrame("Batcheador");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		for(Integer i=0; i<apps.size() ; i++) {
			Class currentApplication = apps.get(i).getClass();
			Annotation[] anotations = currentApplication.getDeclaredAnnotations();
			// procesamos anotations
			// TO DO
			//
			
		    Field[] fields = currentApplication.getDeclaredFields();
		    processFieldsAnnotations(fields, frame);
			break;
		}	
		
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setSize(300,150);
		frame.setVisible(true);
    }
    
    public void processFieldsAnnotations(Field[] fields, JFrame frame) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		for (int i=0; i<fields.length ; i++) {
    		Annotation annotation = fields[i].getAnnotation(Parameter.class);
    		// aca rompe
    		String parameterType = (String) annotation.getClass().getField("type").get(annotation);
    		
    		switch(parameterType) {
    			case "text":
    				JLabel textFieldLabel = new JLabel("Text field", SwingConstants.LEFT);
    		    	textFieldLabel.setPreferredSize(new Dimension(100,30));
    		    	JTextField textField = new JTextField();
    		       
    		    	JPanel panel = new JPanel();
    		    	panel.add(textFieldLabel, BorderLayout.PAGE_START);
    		    	panel.add(textField, BorderLayout.PAGE_END);
    		       
    		    	Container ventana = frame.getContentPane();
    		    	ventana.add(panel, BorderLayout.PAGE_START);
	    		default: 
	    			break;
    		}
    	}
    	
    }
}
