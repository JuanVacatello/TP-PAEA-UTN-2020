package com.utils;

import com.annotations.Parameter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;

public class RunCommand {
    private String commandPath;
    private String commandRootPath;
    private String currentAppCommand;
    final String WINDOWS_COMMAND = "cmd.exe";
    final String WINDOWS_ROOT_PATH = "/c";
    final String LINUX_COMMAND = "bash";
    final String LINUX_ROOT_PATH = "-c";

    public RunCommand(Object currentAppInstance){
        if (System.getProperty("os.name").startsWith("Windows")) {
            commandPath = WINDOWS_COMMAND;
            commandRootPath = WINDOWS_ROOT_PATH;
        } else {
            commandPath = LINUX_COMMAND;
            commandRootPath= LINUX_ROOT_PATH;
        }
        currentAppCommand = build(currentAppInstance);
    }

    private String build(Object currentAppInstance) {
        String appCommand = "";
        String fieldsValues = "";
        appCommand += currentAppInstance.getClass().getAnnotation(com.annotations.Command.class).name();
        Field[] fields = currentAppInstance.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if(fields[i].getAnnotation(Parameter.class) != null)
                fieldsValues+= " " + fields[i].getAnnotation(Parameter.class).flags();
            Object value = null;
            try {
                value = fields[i].get(currentAppInstance);
                fieldsValues+=" "+value;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        appCommand+=fieldsValues;
        return appCommand;
    }

    public void run() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                commandPath, commandRootPath,currentAppCommand);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }
    }

}
