package com.energyxxer.craftr.global;

import com.energyxxer.craftr.files.FileDefaults;
import com.energyxxer.craftr.ui.theme.ThemeManager;
import com.energyxxer.craftr.util.LineReader;
import com.energyxxer.craftrlang.compiler.CraftrLibrary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 1/7/2017.
 */
public class Resources {
    public static final HashMap<String, ArrayList<String>> indexes = new HashMap<>();
    public static final ArrayList<String> tips = new ArrayList<>();

    public static CraftrLibrary nativeLib = null;

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
        } catch(IOException x) {
            x.printStackTrace();
        }

        tips.clear();
        try {
            ArrayList<String> lines = LineReader.read("/resources/tips.txt");
            tips.addAll(lines);
        } catch(IOException x) {
            x.printStackTrace();
        }

        ThemeManager.loadAll();
        FileDefaults.loadAll();

        File nativesFolder = new File(Preferences.get("nativelib"));
        if(!nativesFolder.exists()) nativesFolder.mkdirs();
        else nativeLib = new CraftrLibrary(nativesFolder, "Craftr Native Library");
    }
}
