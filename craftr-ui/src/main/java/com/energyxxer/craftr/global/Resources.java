package com.energyxxer.craftr.global;

import com.energyxxer.craftr.files.FileDefaults;
import com.energyxxer.craftr.ui.theme.ThemeManager;
import com.energyxxer.craftr.util.LineReader;
import com.energyxxer.craftrlang.compiler.NativeLib;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 1/7/2017.
 */
public class Resources {
    public static final HashMap<String, ArrayList<String>> indexes = new HashMap<>();

    public static void load() {
        indexes.clear();
        try {
            ArrayList<String> lines = LineReader.read("/resources/indexes.txt");
            String key = null;
            ArrayList<String> currentValues = null;
            for(String line : lines) {
                if(line.endsWith(":")) {
                    if(key != null) indexes.put(key, currentValues);
                    currentValues = new ArrayList<>();

                    key = line.substring(0,line.lastIndexOf(":"));
                } else if(line.startsWith("-")) {
                    if(key != null) {
                        currentValues.add(line.substring(1).trim());
                    }
                }
            }
            if(key != null) indexes.put(key, currentValues);
        } catch(IOException e) {
            e.printStackTrace();
        }

        ThemeManager.loadAll();
        FileDefaults.loadAll();

        File nativesFolder = new File(System.getProperty("user.home") + File.separator + "Craftr" + File.separator + "natives" + File.separator);
        if(!nativesFolder.exists()) nativesFolder.mkdirs();
        else NativeLib.setLib(nativesFolder);
    }
}
