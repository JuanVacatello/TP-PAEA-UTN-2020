package com.batcheador;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.*;

public class Batcheador {
    public static void createWindow() {
    	JFrame frame = new JFrame("Batcheador");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        
        JLabel comboBoxLabel = new JLabel("Combo box", SwingConstants.LEFT);
        comboBoxLabel.setPreferredSize(new Dimension(100,30));
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("Opcion 1");
        comboBox.addItem("Opcion 2");
        comboBox.addItem("Opcion 3");
        comboBox.setPreferredSize(new Dimension(100,30));

        JLabel textFieldLabel = new JLabel("Text field", SwingConstants.LEFT);
        textFieldLabel.setPreferredSize(new Dimension(100,30));
        JTextField textField = new JTextField();
        textField.setEditable(false);
        
        comboBox.addItemListener(new ItemListener() {
            /*
             * Funcion para sobreescribir el textField ante el evento
             * de cambio de seleccion en el comboBox(listener)
             */
        	@Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedOption = (String) comboBox.getSelectedItem();
                    textField.setText(selectedOption);
                    System.out.println(selectedOption);
                }
            }
        });
        textField.setPreferredSize(new Dimension(100,30));
		
        final JFileChooser fc = new JFileChooser();
        JLabel fileChooserLabel = new JLabel("File chooser", SwingConstants.LEFT);
        fileChooserLabel.setPreferredSize(new Dimension(100,30));
        JButton fileChooserButton = new JButton("Examinar");
        
        fileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int returnVal = fc.showOpenDialog(frame);
            	 
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                	File file = fc.getSelectedFile();
                	System.out.println(file.getName());
                } else {
                	System.out.println("El usuario canceló");
                }
            }
        });
        
        fileChooserButton.setPreferredSize(new Dimension(100,30));
   
        panel.add(comboBoxLabel, BorderLayout.PAGE_START);
        panel.add(comboBox, BorderLayout.PAGE_END);
        
        JPanel panel2 = new JPanel();
        panel2.add(textFieldLabel, BorderLayout.PAGE_START);
        panel2.add(textField, BorderLayout.PAGE_END);
        
        JPanel panel3 = new JPanel();
        panel3.add(fileChooserLabel, BorderLayout.PAGE_START);
        panel3.add(fileChooserButton, BorderLayout.PAGE_END);
        
        Container ventana = frame.getContentPane();
        ventana.add(panel, BorderLayout.PAGE_START);
        ventana.add(panel2, BorderLayout.CENTER);
        ventana.add(panel3, BorderLayout.PAGE_END);
        
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setSize(300,150);
        frame.setVisible(true);
    
    }
}
