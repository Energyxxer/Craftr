package com.energyxxer.craftr.util;

import com.energyxxer.util.out.Console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by User on 1/21/2017.
 */
public class ResourceReader {
    public static String read(String file) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(ResourceReader.class.getResourceAsStream(file)))) {
            StringBuilder sb = new StringBuilder();
            String line;
            for (; (line = br.readLine()) != null; ) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch(NullPointerException x) {
            x.printStackTrace();
            Console.err.println("[ERROR] File not found: " + file);
        } catch(IOException x) {
            Console.err.println("[ERROR] Unable to access file: " + file);
        }
        return "";
    }
    
    private ResourceReader() {}
}
