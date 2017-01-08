package com.energyxxer.cbe.ui.theme;

import com.energyxxer.cbe.util.LineReader;
import com.energyxxer.cbe.util.Range;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 12/13/2016.
 */
public class ThemeReader {

    private HashMap<String, Object> themeValues;

    private int currentLine = 0;
    private String line;

    public Theme read(String name) throws ThemeParserException {
        themeValues = new HashMap<>();
        try {
            ArrayList<String> lines = LineReader.read("/resources/themes/" + name + ".properties");
            for(String line : lines) {
                currentLine++;
                line = line.trim();
                if(line.length() == 0) continue;
                if(line.startsWith("#")) continue;
                if(!line.contains("=")) throw new ThemeParserException("Couldn't find key/value separator.",currentLine,line);

                String key = line.substring(0,line.indexOf("=")).trim();
                String valueString = line.substring(line.indexOf("=")+1).trim();

                if(key.length() == 0) throw new ThemeParserException("Couldn't find key.",currentLine,line);
                if(valueString.length() == 0) throw new ThemeParserException("Couldn't find value.",currentLine,line);

                Object value = parseValue(valueString);
                themeValues.put(key,value);
            }
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
        return new Theme(name,themeValues);
    }

    private Object parseValue(String value) throws ThemeParserException {

        if(value.startsWith("rgb(")) {
            if(value.endsWith(")")) {
                String inner = value.substring(4,value.length()-1);
                String[] entryList = inner.split(",");
                if(!new Range(3,4).contains(entryList.length)) throw new ThemeParserException("Color values take only 3 or 4 arguments.",currentLine,line);
                int[] colorValues = { 0, 0, 0, 255 };
                for(int i = 0; i < 4; i++) {
                    if(i >= entryList.length) break;
                    String entry = entryList[i].trim();
                    try {
                        if(i < 3) {
                            int colorValue = Integer.parseInt(entry);
                            if(colorValue < 0 || colorValue > 255) throw new ThemeParserException("Color values must be in the 0 and 255 range.",currentLine,line);
                            colorValues[i] = colorValue;
                        } else {
                            float colorValue = Float.parseFloat(entry);
                            if(colorValue < 0 || colorValue > 1) throw new ThemeParserException("Color alpha must be between 0 and 1",currentLine,line);
                            colorValues[i] = Math.round(colorValue*255);
                        }
                    } catch(NumberFormatException e) {
                        throw new ThemeParserException("Expected a number, instead got \"" + entryList[i].trim() + "\"",currentLine,line);
                    }
                }
                return new Color(colorValues[0],colorValues[1],colorValues[2],colorValues[3]);
            } else throw new ThemeParserException("Couldn't find end of color literal.",currentLine,line);
        } else if(value.startsWith("#")) {
            try {
                return new Color(Integer.parseInt(value.substring(1),16));
            } catch(NumberFormatException e) {
                throw new ThemeParserException("Expected a hexadecimal color, instead got \"" + value.substring(1) + "\"",currentLine,line);
            }
        } else if(value.startsWith("@")) {
            if(value.length() == 1) throw new ThemeParserException("Unterminated key reference. Expected name.",currentLine,line);
            Object obj = themeValues.get(value.substring(1));
            if(obj == null) throw new ThemeParserException("Invalid key reference. \"" + value.substring(1) + "\" hasn't been defined at this point in the file",currentLine,line);
            return obj;
        } else {
            return value;
        }
    }

}
