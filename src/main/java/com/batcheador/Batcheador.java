package com.batcheador;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.NumberFormatter;

import com.annotations.*;
import com.utils.*;
import org.reflections.Reflections;

public class Batcheador {
	private final List<Class> apps;

	public Batcheador() {
		Reflections reflections = new Reflections("");
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Command.class);
		this.apps = new ArrayList<Class>(annotated);
		CommandRunner.init();
	}

	public void createWindow() {
		JFrame mainFrame = new JFrame("Batcheador");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panelComboBoxLabel = new JPanel();
		JPanel panelComboBox = new JPanel();

		JLabel comboBoxLabel = new JLabel("¿Qué funcionalidad desea?", SwingConstants.LEFT);
		comboBoxLabel.setPreferredSize(new Dimension(200, 30));

		String[] applications = new String[apps.size()];
		for (int i = 0; i < apps.size(); i++) {
			applications[i] = ((Application) apps.get(i).getAnnotation(Application.class)).name();
		}

		JComboBox comboBox = new JComboBox(applications);
		comboBox.setSelectedIndex(-1);
		comboBox.setPreferredSize(new Dimension(200, 30));

		panelComboBoxLabel.add(comboBoxLabel);
		panelComboBox.add(comboBox);

		mainFrame.add(panelComboBoxLabel, BorderLayout.PAGE_START);
		mainFrame.add(panelComboBox, BorderLayout.CENTER);

		comboBox.addActionListener(event -> {
			JComboBox comboBox1 = (JComboBox) event.getSource();
			Object selected = comboBox1.getSelectedItem();

			String appName = selected.toString();
			JFrame appFrame = new JFrame(appName);

			Class currentApplication = apps.get(comboBox1.getSelectedIndex());

			try {
				processApp(appName, currentApplication, appFrame, mainFrame);
			} catch (Exception error) {
				System.out.println(error.toString());
			}
			mainFrame.setVisible(false);

			appFrame.setLocationRelativeTo(null);
			appFrame.pack();
			appFrame.setVisible(true);
		});

		mainFrame.setLocationRelativeTo(null);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	public void processApp(String appName, Class currentApplication, JFrame appFrame, JFrame mainFrame) throws Exception {
		Container window = appFrame.getContentPane();
		window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));

		Field[] fields = currentApplication.getDeclaredFields();
		Object currentAppInstance = currentApplication.getDeclaredConstructor().newInstance();

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);

			Annotation annotation = field.getAnnotation(Parameter.class);
			Method typeGetter = annotation.annotationType().getDeclaredMethod("type");
			String parameterType = (String) typeGetter.invoke(annotation, (Object[]) null);
			Method labelGetter = annotation.annotationType().getDeclaredMethod("label");
			String parameterLabel = (String) labelGetter.invoke(annotation, (Object[]) null);

			switch (parameterType) {
				case "audioCodec":
					renderCodec(parameterLabel, window, field, currentAppInstance, "audio");
					break;
				case "videoCodec":
					renderCodec(parameterLabel, window, field, currentAppInstance, "video");
					break;
				case "file":
					renderFileChooser(parameterLabel, window, field, currentAppInstance);
					break;
				case "text":
					renderText(parameterLabel, window, field, currentAppInstance);
					break;
				case "number":
					renderNumber(parameterLabel, window, field, currentAppInstance);
					break;
				case "outputFile":
					renderOutputFile(parameterLabel, window, field, currentAppInstance);
					break;
				default:
					break;
			}
		}

		JButton cancelButton = new JButton("Cancelar");
		JButton acceptButton = new JButton("Confirmar");
		acceptButton.setEnabled(false);
		JPanel footerPanelButton = new JPanel();

		footerPanelButton.add(cancelButton, BorderLayout.PAGE_START);
		footerPanelButton.add(acceptButton, BorderLayout.PAGE_END);

		acceptButton.addActionListener(e -> {
			System.out.println("App name: " + appName);

			printClass(currentAppInstance);

			try {
				List<String> errors = CommandRunner.run(currentAppInstance);

				showResultsWindow(appFrame, errors);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		cancelButton.addActionListener(e -> {
			appFrame.setVisible(false);
			mainFrame.setVisible(true);
		});

		window.add(footerPanelButton);
	}

	private void addContentToResultsPanel(JPanel contentPanel, List<String> errors) {
		if (errors.size() != 0) {
			JLabel errorSectionTitle = new JLabel("Hubo errores al ejecutar el comando:");

			JPanel errorsPanel = new JPanel();
			errorsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
			errorsPanel.setLayout(new BoxLayout(errorsPanel, BoxLayout.Y_AXIS));

			for( int i = 0; i < errors.size(); i++) {
				JLabel errorLabel = new JLabel(errors.get(i));
				errorsPanel.add(errorLabel);
			}

			contentPanel.add(errorSectionTitle);
			contentPanel.add(errorsPanel);
		} else {
			JLabel successLabel = new JLabel("El comando se ejecuto exitosamente");
			contentPanel.add(successLabel);
		}
	}

	private void showResultsWindow(JFrame appFrame, List<String> errors) {
		JFrame resultsFrame = new JFrame("Resultados");
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		addContentToResultsPanel(contentPanel, errors);

		resultsFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				resultsFrame.setVisible(false);
				appFrame.setVisible(true);
			}
		});

		resultsFrame.add(contentPanel, BorderLayout.CENTER);
		resultsFrame.setLocationRelativeTo(null);
		resultsFrame.pack();
		appFrame.setVisible(false);
		resultsFrame.setVisible(true);
	}

	private void setValueOnField(Object value, Field field, Object currentAppInstance) {
		try {
			field.set(currentAppInstance, value);
		} catch (IllegalAccessException error) {
			System.out.println(error.toString());
		}
	}

	private Object getDocumentValue(DocumentEvent event, Boolean isNumberTextField) {
		Document document = event.getDocument();
		String value = "some error";

		try {
			value = document.getText(0, document.getLength());
		} catch (BadLocationException DocumentEvent) {
			System.out.println(DocumentEvent.toString());
		}

		if (isNumberTextField) {
			return value.equals("") ? 0 : Integer.parseInt(value);
		}

		return value;
	}

	private void addListenerToTextField(JTextField textField, Field field, Object currentAppInstance, Boolean isNumberTextField, Container window) {
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent event) {
				setValueOnField(getDocumentValue(event, isNumberTextField), field, currentAppInstance);
				validateConfirmar(window, currentAppInstance);
			}

			public void removeUpdate(DocumentEvent event) {
				Object value = getDocumentValue(event, isNumberTextField);

				setValueOnField(value, field, currentAppInstance);
				validateConfirmar(window, currentAppInstance);
			}

			public void insertUpdate(DocumentEvent event) {
				setValueOnField(getDocumentValue(event, isNumberTextField), field, currentAppInstance);
				validateConfirmar(window, currentAppInstance);
			}
		});
	}

	private String[] getCodecs(String codecType) {
		String[] audioCodecs = {"flac", "mp3", "wma", "aac", "libvo_aacenc", "copy", "amr"};
		String[] videoCodecs = {"libx264", "copy", "mpeg4", "flv", "wmv1", "libxvid"};

		return codecType.equals("audio") ? audioCodecs : videoCodecs;
	}

	public static Method findMethodOnOutputFile(Object outputFile, String methodName) {
		Method method = null;
		try {
			Method[] methods = outputFile.getClass().getDeclaredMethods();

			for (Method currentMethod : methods) {
				if (currentMethod.getName().equals(methodName)) {
					method = currentMethod;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return method;
	}

	private void setValueOnOutputFile(Object currentOutputFile, String methodName, String value) {
		try {
			Method setFolderPathMethod = findMethodOnOutputFile(currentOutputFile, methodName);

			setFolderPathMethod.setAccessible(true);
			setFolderPathMethod.invoke(currentOutputFile, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void renderCodec(String parameterLabel, Container window, Field field, Object currentAppInstance, String codecType) {
		String[] codecs = getCodecs(codecType);

		JPanel panelComboBox = new JPanel();

		JComboBox comboBox = new JComboBox(codecs);
		setValueOnField(codecs[0], field, currentAppInstance);

		comboBox.setPreferredSize(new Dimension(200, 30));

		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox comboBox = (JComboBox) event.getSource();
				Object selected = comboBox.getSelectedItem();

				setValueOnField(selected.toString(), field, currentAppInstance);

				validateConfirmar(window, currentAppInstance);
			}
		});

		JLabel comboBoxLabel = new JLabel(parameterLabel, SwingConstants.LEFT);
		comboBoxLabel.setPreferredSize(new Dimension(100, 30));

		panelComboBox.add(comboBoxLabel, BorderLayout.PAGE_START);
		panelComboBox.add(comboBox, BorderLayout.PAGE_END);
		window.add(panelComboBox);
	}

	private void renderText(String parameterLabel, Container window, Field field, Object currentAppInstance) {
		JLabel textFieldLabel = new JLabel(parameterLabel, SwingConstants.LEFT);
		textFieldLabel.setPreferredSize(new Dimension(200, 30));
		JTextField textField = new JTextField();
		textField.setPreferredSize(new Dimension(100, 30));

		addListenerToTextField(textField, field, currentAppInstance, false, window);

		JPanel panel = new JPanel();
		panel.add(textFieldLabel, BorderLayout.PAGE_START);
		panel.add(textField, BorderLayout.PAGE_END);

		window.add(panel);
	}

	private void renderNumber(String parameterLabel, Container window, Field field, Object currentAppInstance) {
		JLabel textFieldLabel = new JLabel(parameterLabel, SwingConstants.LEFT);
		textFieldLabel.setPreferredSize(new Dimension(200, 30));

		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setGroupingUsed(false);

		NumberFormatter numberFormatter = new NumberFormatter(format);
		numberFormatter.setValueClass(Long.class);
		numberFormatter.setAllowsInvalid(false);

		JFormattedTextField jFormattedTextField = new JFormattedTextField(numberFormatter);
		jFormattedTextField.setPreferredSize(new Dimension(100, 30));

		addListenerToTextField(jFormattedTextField, field, currentAppInstance, true, window);

		JPanel panel = new JPanel();
		panel.add(textFieldLabel, BorderLayout.PAGE_START);
		panel.add(jFormattedTextField, BorderLayout.PAGE_END);

		window.add(panel);
	}

	private void renderFileChooser(String parameterLabel, Container window, Field field, Object currentAppInstance) {
		final JFileChooser fileChooser = new JFileChooser();
		JLabel fileChooserLabel = new JLabel(parameterLabel, SwingConstants.LEFT);
		JButton fileChooserButton = new JButton("Examinar");

		JLabel filePathLabel = new JLabel();
		filePathLabel.setMinimumSize(new Dimension(100, 0));

		fileChooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(window);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filePath = fileChooser.getSelectedFile().getAbsolutePath();

					filePathLabel.setText(filePath);
					setValueOnField(filePath, field, currentAppInstance);
				}

				validateConfirmar(window, currentAppInstance);
			}
		});

		fileChooserButton.setPreferredSize(new Dimension(100, 30));

		JPanel panel = new JPanel();
		panel.add(fileChooserLabel, BorderLayout.PAGE_START);
		panel.add(fileChooserButton, BorderLayout.CENTER);
		panel.add(filePathLabel, BorderLayout.PAGE_END);

		window.add(panel);
	}

	private void renderOutputFile(String parameterLabel, Container window, Field field, Object currentAppInstance) throws Exception {
		Object currentOutputFile = field.getType().getDeclaredConstructor().newInstance();
		field.set(currentAppInstance, currentOutputFile);

		final JFileChooser outputFileLocationChooser = new JFileChooser();
		JLabel outputFileNameLabel = new JLabel(parameterLabel, SwingConstants.LEFT);
		outputFileNameLabel.setPreferredSize(new Dimension(200, 30));
		JButton outputFileLocationChooserButton = new JButton("Ubicacion");

		outputFileLocationChooser.setCurrentDirectory(new File("."));
		outputFileLocationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		outputFileLocationChooser.setAcceptAllFileFilterUsed(false);

		JLabel folderPathLabel = new JLabel();
		folderPathLabel.setMinimumSize(new Dimension(150, 0));

		outputFileLocationChooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = outputFileLocationChooser.showOpenDialog(window);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String folderPath = outputFileLocationChooser.getSelectedFile().getAbsolutePath();

					folderPathLabel.setText(folderPath);

					setValueOnOutputFile(currentOutputFile, "setFolderPath", folderPath);
				}

				validateConfirmar(window, currentAppInstance);
			}
		});

		outputFileLocationChooserButton.setPreferredSize(new Dimension(100, 30));

		JTextField fileNameTextField = new JTextField();
		fileNameTextField.setPreferredSize(new Dimension(100, 30));
		fileNameTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent event) {
				setValueOnOutputFile(currentOutputFile, "setFileName", (String) getDocumentValue(event, false));
				validateConfirmar(window, currentAppInstance);
			}

			public void removeUpdate(DocumentEvent event) {
				setValueOnOutputFile(currentOutputFile, "setFileName", (String) getDocumentValue(event, false));
				validateConfirmar(window, currentAppInstance);
			}

			public void insertUpdate(DocumentEvent event) {
				setValueOnOutputFile(currentOutputFile, "setFileName", (String) getDocumentValue(event, false));
				validateConfirmar(window, currentAppInstance);
			}
		});

		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();

		topPanel.add(outputFileNameLabel, BorderLayout.PAGE_START);
		topPanel.add(fileNameTextField, BorderLayout.PAGE_END);
		bottomPanel.add(outputFileLocationChooserButton, BorderLayout.PAGE_START);
		bottomPanel.add(folderPathLabel, BorderLayout.PAGE_END);

		window.add(topPanel);
		window.add(bottomPanel);
	}

	private void printClass (Object currentAppInstance)  {
		Field[] fields = currentAppInstance.getClass().getDeclaredFields();
		Arrays.stream(fields).forEach(field -> {
			field.setAccessible(true);
			String name = field.getName();
			Object value = null;

			try {
				value = field.get(currentAppInstance);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			System.out.println(name + ": " + value);
		});

	}

	private void validateConfirmar(Container window, Object currentApp) {
		for (int i = 0; i < window.getComponents().length; i++) {
			if (window.getComponent(i) instanceof JPanel) {
				JPanel panel = (JPanel) window.getComponent(i);
				for (int j = 0; j < panel.getComponents().length; j++) {
					if (panel.getComponent(j) instanceof JButton) {
						JButton button = (JButton) panel.getComponent(j);
						if (button.getText().equals("Confirmar"))
							button.setEnabled(Validator.IsValid(currentApp));
					}
				}
			}
		}
	}
}