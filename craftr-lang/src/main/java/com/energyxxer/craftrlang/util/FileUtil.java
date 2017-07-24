package com.energyxxer.craftrlang.util;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Created by User on 4/9/2017.
 */
public class FileUtil {
    public static String getRelativePath(File file, File root) {
        String result = (file.getAbsolutePath() + File.separator).replaceFirst(Matcher.quoteReplacement(root.getAbsolutePath() + File.separator),"");
        if(result.endsWith(File.separator)) result = result.substring(0, result.length()-1);
        return result;
    }

    public static String stripExtension(String str) {

        if (str == null)
            return null;

        int pos = str.lastIndexOf(".");

        if (pos == -1)
            return str;

        return str.substring(0, pos);
    }

    private FileUtil() {}
}
