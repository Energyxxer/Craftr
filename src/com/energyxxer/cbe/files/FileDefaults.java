package com.energyxxer.cbe.files;

import com.energyxxer.cbe.util.ResourceReader;

import java.util.HashMap;

/**
 * Created by User on 1/21/2017.
 */
public class FileDefaults {
    public static final HashMap<String, String> defaults = new HashMap<>();

    private static final String[] indexes = "entity, item, feature, class, world".split(", ");

    public static void loadAll() {
        defaults.clear();

        for(String name : indexes) {
            defaults.put(name, ResourceReader.read("/resources/defaults/" + name + ".txt").replace("\t","    "));
        }
    }
}
