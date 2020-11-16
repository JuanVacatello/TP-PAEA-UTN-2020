package com.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class cmdtest {
    public static void main(String args[]) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe","/c","ffmpeg -i C:\\Users\\Juan\\Downloads\\Video-2.mp4 -c copy -an C:\\Users\\Juan\\Downloads\\muteado.mp4");
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
