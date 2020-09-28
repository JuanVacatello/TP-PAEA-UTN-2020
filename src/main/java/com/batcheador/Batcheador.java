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
import java.util.List;

import javax.swing.*;
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

					JPanel panel2 = new JPanel();
					panel2.add(fileChooserLabel, BorderLayout.PAGE_START);
					panel2.add(fileChooserButton, BorderLayout.PAGE_END);
					ventana.add(panel2);
					break;
    			case "text":
    				JLabel textFieldLabel = new JLabel(parameterLabel, SwingConstants.LEFT);
    		    	textFieldLabel.setPreferredSize(new Dimension(200,30));
    		    	JTextField textField = new JTextField();

    		    	JPanel panel = new JPanel();
    		    	panel.add(textFieldLabel, BorderLayout.PAGE_START);
    		    	panel.add(textField, BorderLayout.PAGE_END);

    		    	ventana.add(panel);
    		    	break;
				case "number":
					JLabel textFieldLabel2 = new JLabel(parameterLabel, SwingConstants.LEFT);
					textFieldLabel2.setPreferredSize(new Dimension(200,30));

					NumberFormat format = NumberFormat.getIntegerInstance();
					format.setGroupingUsed(false);

					NumberFormatter numberFormatter = new NumberFormatter(format);
					numberFormatter.setValueClass(Long.class);
					numberFormatter.setAllowsInvalid(false);

					JFormattedTextField jFormattedTextField = new JFormattedTextField(numberFormatter);
					jFormattedTextField.setPreferredSize(new Dimension(200,30));

					JPanel panel3 = new JPanel();
					panel3.add(textFieldLabel2, BorderLayout.PAGE_START);
					panel3.add(jFormattedTextField, BorderLayout.PAGE_END);

					ventana.add(panel3);
					break;
	    		default:
	    			break;
    		}
    	}
    	
    }
}
