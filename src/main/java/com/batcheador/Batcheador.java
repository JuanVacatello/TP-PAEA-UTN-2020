package com.batcheador;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;

import com.annotations.*;

public class Batcheador {
	private List<Class> apps;

	public Batcheador(List<Class> applications) {
			this.apps = applications;
	}
	
    public void createWindow() throws Exception {
		JFrame frame = new JFrame("Batcheador");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
/*
		JPanel panelcomboBoxLabel = new JPanel();
		JPanel panelcomboBox = new JPanel();
		JPanel panelacceptButton = new JPanel();

		JLabel comboBoxLabel = new JLabel("¿Qué funcionalidad desea?", SwingConstants.LEFT);
		comboBoxLabel.setPreferredSize(new Dimension(200,30));


		String[] applications = new String[apps.size()];
		for(int i = 0; i<apps.size(); i++){
			applications[i] = apps.get(i).getSimpleName();

			//String applicationName = apps.get(i).getClass().getDeclaredAnnotations();
			//System.out.println(annotation.toString());
			//System.out.println(applications[i]);
			//applications[i] = applicationName;
			//System.out.println(applications[i]);

		}

		JComboBox comboBox = new JComboBox(applications);
		comboBox.setPreferredSize(new Dimension(200,30));

		JButton acceptButton = new JButton("Confirmar");

		panelcomboBoxLabel.add(comboBoxLabel);
		panelcomboBox.add(comboBox);
		panelacceptButton.add(acceptButton);

		frame.add(panelcomboBoxLabel, BorderLayout.PAGE_START);
		frame.add(panelcomboBox, BorderLayout.CENTER);
		frame.add(panelacceptButton, BorderLayout.PAGE_END);

*/
		for(Integer i=0; i<apps.size() ; i++) {
			Class currentApplication = apps.get(i);
			Annotation[] anotations = currentApplication.getDeclaredAnnotations();
			//TODO: procesar annotations
		    Field[] fields = currentApplication.getDeclaredFields();
		    processFieldsAnnotations(fields, frame);
			break;
		}

		frame.setLocationRelativeTo(null);
		frame.setSize(500,200);
		frame.pack();
		frame.setVisible(true);
    }
    
    public void processFieldsAnnotations(Field[] fields, JFrame frame) throws Exception {
		Container ventana = frame.getContentPane();
		ventana.setLayout(new BoxLayout(ventana, BoxLayout.Y_AXIS));
		for (int i=0; i<fields.length ; i++) {
 			fields[i].setAccessible(true);
    		Annotation annotation = fields[i].getAnnotation(Parameter.class);
			Method typeGetter = annotation.annotationType().getDeclaredMethod("type");
			String parameterType = (String)typeGetter.invoke(annotation, (Object[]) null);
			Method labelGetter = annotation.annotationType().getDeclaredMethod("label");
			String parameterLabel = (String)labelGetter.invoke(annotation, (Object[]) null);
			switch(parameterType) {
				case "file":
					renderFileChooser(parameterLabel, ventana, frame);
					break;
    			case "text":
    				renderText(parameterLabel, ventana);
    		    	break;
				case "number":
					renderNumber(parameterLabel, ventana);
					break;
	    		default:
	    			break;
    		}
    	}	
    }
    
    public void renderText(String parameterLabel, Container ventana) {
    	JLabel textFieldLabel = new JLabel(parameterLabel, SwingConstants.LEFT);
    	textFieldLabel.setPreferredSize(new Dimension(200,30));
    	JTextField textField = new JTextField();

    	JPanel panel = new JPanel();
    	panel.add(textFieldLabel, BorderLayout.PAGE_START);
    	panel.add(textField, BorderLayout.PAGE_END);

    	ventana.add(panel);
    }
    
    public void renderNumber(String parameterLabel, Container ventana) {
    	JLabel textFieldLabel = new JLabel(parameterLabel, SwingConstants.LEFT);
		textFieldLabel.setPreferredSize(new Dimension(200,30));

		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setGroupingUsed(false);

		NumberFormatter numberFormatter = new NumberFormatter(format);
		numberFormatter.setValueClass(Long.class);
		numberFormatter.setAllowsInvalid(false);

		JFormattedTextField jFormattedTextField = new JFormattedTextField(numberFormatter);
		jFormattedTextField.setPreferredSize(new Dimension(200,30));

		JPanel panel = new JPanel();
		panel.add(textFieldLabel, BorderLayout.PAGE_START);
		panel.add(jFormattedTextField, BorderLayout.PAGE_END);

		ventana.add(panel);
    }
    
    public void renderFileChooser(String parameterLabel, Container ventana, JFrame frame) {
    	final JFileChooser fc = new JFileChooser();
		JLabel fileChooserLabel = new JLabel(parameterLabel, SwingConstants.LEFT);
		fileChooserLabel.setPreferredSize(new Dimension(200,30));
		JButton fileChooserButton = new JButton("Examinar");

		fileChooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					System.out.println(file.getName());
				} else {
					System.out.println("No aprobado ?");
				}
			}
		});

		fileChooserButton.setPreferredSize(new Dimension(100,30));

		JPanel panel = new JPanel();
		panel.add(fileChooserLabel, BorderLayout.PAGE_START);
		panel.add(fileChooserButton, BorderLayout.PAGE_END);
		ventana.add(panel);
    }
    
}
