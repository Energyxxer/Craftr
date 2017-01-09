package com.energyxxer.cbe.util;

import com.energyxxer.cbe.global.Console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by User on 1/8/2017.
 */
public class LineReader {
    public static ArrayList<String> read(String file) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(file)))) {
            String line;
            for (; (line = br.readLine()) != null; ) {
                if(line.length() > 0 && !line.startsWith("#")) lines.add(line.trim());
            }
            return lines;
        } catch(NullPointerException npe) {
            Console.err.println("[ERROR] File not found: " + file);
            return new ArrayList<>();
        }
    }
}