package com.batcheador;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.annotations.Command;
import org.reflections.Reflections;

public class Main {
    public static void main(String[] args) {
    	List<Class> applications = new ArrayList<Class>();

//		La idea del ComboBox con opciones era que fuera genérico,
//		que el usuario final pueda agregar su propio combobox sin tener que meterse al código de la biblioteca.
//		También estaría bueno que el atributo type fuera un enum, así el usuario puede crear sus comandos con autocomplete súper fácil.
//		Como ya lo implementaron así, y el resto del TP está bastante bien, no se hagan problema por esto. Si tienen tiempo y ganas,
//		estaría bueno como algo opcional que lo corrijan.

//		Entrega 4 2020-11-11
//		Requerimientos
//		TODO:Debe agregarse una anotación para indicar aquellas clases que son comandos. x
//		TODO:Debe usarse una biblioteca de terceros para encontrar todas las clases con la anotación.x
//		y cargarlas al batcheador automáticamente, al inicio del programa, sin pasar ninguna lista a mano.
//		(en el futuro habrá un instructivo sobre esta biblioteca)
//		TODO:El botón de confirmación debe estar deshabilitado si existen campos vacíos o campos con contenido inválido. x
//		TODO:Al oprimir el botón de confirmación, debe ejecutarse el comando de FFmpeg.
//		TODO:Debe detectar y notificar la existencia de un error de FFmpeg (ver códigos de error).
//		TODO:No hace falta manejarlo ni parsarlo, con mostrar la salida de FFmpeg al usuario es suficiente.
//		TODO:Deben estar implementados los tres comandos del ejemplo al final del documento.


		Reflections reflections = new Reflections("");
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Command.class);

		applications.addAll(annotated);

    	Batcheador batcheador = new Batcheador(applications);
    	batcheador.createWindow();
    }
}
