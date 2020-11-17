package com.utils;

import com.annotations.Parameter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
        return line.contains("Error") || line.contains("Unable");
    }

    private static String build(Object currentAppInstance) {
        String appCommand = "";
        String fieldsValues = "";
        appCommand += currentAppInstance.getClass().getAnnotation(com.annotations.Command.class).name();
        Field[] fields = currentAppInstance.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if(fields[i].getAnnotation(Parameter.class) != null) {
                fieldsValues += " " + fields[i].getAnnotation(Parameter.class).flags();
                Object value = null;
                try {
                    value = fields[i].get(currentAppInstance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                fieldsValues += " " + value;
            }
        }
        appCommand+=fieldsValues;
        return appCommand;
    }
}
