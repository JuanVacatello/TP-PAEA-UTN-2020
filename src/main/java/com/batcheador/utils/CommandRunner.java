package com.batcheador.utils;

import com.batcheador.annotations.*;
import com.batcheador.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CommandRunner {
    private static String commandPath;
    private static String commandRootPath;
    final static String WINDOWS_COMMAND = "cmd.exe";
    final static String WINDOWS_ROOT_PATH = "/c";
    final static String LINUX_COMMAND = "bash";
    final static String LINUX_ROOT_PATH = "-c";

    public static void init() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            commandPath = WINDOWS_COMMAND;
            commandRootPath = WINDOWS_ROOT_PATH;
        } else {
            commandPath = LINUX_COMMAND;
            commandRootPath= LINUX_ROOT_PATH;
        }
    }

    public static List<String> run(Object currentAppInstance) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(commandPath, commandRootPath, build(currentAppInstance));
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        List<String> errors = new ArrayList<>();
        String line;
        while ((line = r.readLine()) != null) {
            if (hasError(line))
                errors.add(line);
            System.out.println(line);
        }

        return errors;
    }

    private static boolean hasError(String line) {
        // TODO: No se como reconocer cuando ffmpeg falla, deberiamos agregar aca ese checkeo y sacar esto horrible.
        return line.contains("Error") || line.contains("Unable") ;
    }

    private static String build(Object currentAppInstance) {
        String appCommand = currentAppInstance.getClass().getAnnotation(Application.class).command();
        String fieldsValues = "";
        Field[] fields = currentAppInstance.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Parameter parameterAnnotation = field.getAnnotation(Parameter.class);
            if (parameterAnnotation != null) {
                fieldsValues += parameterAnnotation.prefix().isEmpty() ? "" : " " + parameterAnnotation.prefix() ;
                fieldsValues += " " + parameterAnnotation.flags();
                Object value = null;
                try {
                    value = field.get(currentAppInstance);
                    if (value instanceof OutputFile) {
                        Method getFolderPathMethod = Batcheador.findMethodOnOutputFile(value, "getFolderPath");
                        Method getFileNameMethod = Batcheador.findMethodOnOutputFile(value, "getFileName");
                        getFolderPathMethod.setAccessible(true);
                        getFileNameMethod.setAccessible(true);

                        String folderPath = (String) getFolderPathMethod.invoke(value);
                        String fileName = (String) getFileNameMethod.invoke(value);

                        value = folderPath + "/" + fileName;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (value instanceof String && ((String) value).contains(" ")) {
                    value = "\"" + value + "\"";
                }
                fieldsValues += " " + value;
                fieldsValues += parameterAnnotation.suffix().isEmpty() ? "" : " " + parameterAnnotation.suffix();
            }
        }
        appCommand += fieldsValues;
        System.out.println(appCommand);
        return appCommand;
    }
}
