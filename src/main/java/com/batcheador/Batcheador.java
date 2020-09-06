package com.batcheador;
import java.awt.*; 
import javax.swing.*;

public class Batcheador {
    public static void createWindow() {
        /*
         * Creo ventana(JFrame)
         */
    	JFrame frame = new JFrame("Batcheador");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /*
         * Creo labels y sus correspondientes componentes, esto
         * deberia hacerse en base a los parametro que deberia recibir el
         * batcheador. 
         */
        JLabel comboBoxLabel = new JLabel("Combo box", SwingConstants.LEFT);
        comboBoxLabel.setPreferredSize(new Dimension(100,50));
        // Acá habria que agregar un JComboBox
        
        JLabel textFieldLabel = new JLabel("Text field", SwingConstants.LEFT);
        textFieldLabel.setPreferredSize(new Dimension(100,50));
        // Acá habria que agregar un JTextField
		
        JLabel fileChooserLabel = new JLabel("File chooser", SwingConstants.LEFT);
        fileChooserLabel.setPreferredSize(new Dimension(100,50));
        // Acá habria que agregar un JButton que genere un JFileChooser 
        
        /*
         * Agrego todo lo que cree al frame(ventana)
         */
        frame.getContentPane().add(comboBoxLabel, BorderLayout.PAGE_START);
        frame.getContentPane().add(textFieldLabel, BorderLayout.CENTER);
        frame.getContentPane().add(fileChooserLabel, BorderLayout.PAGE_END);
        
        
        /*
         * seteo , hago un pack(?) creo que para que se valide todo
         * lo que agregué o no sé, no lei bien la docu pero se que hay que hacerlo
         * y lo muestro
         */
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setSize(100,150);
        frame.setVisible(true);
    
    }
}
