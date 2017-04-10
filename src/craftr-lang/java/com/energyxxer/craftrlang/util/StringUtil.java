package com.energyxxer.craftrlang.util;

/**
 * Created by User on 4/9/2017.
 */
public class StringUtil {
    public static String getInitials(String s) {
        StringBuilder initials = new StringBuilder();

        char lastChar = 0;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0) {
                initials.append(s.charAt(i));
            } else if(Character.isUpperCase(s.charAt(i))) {
                initials.append(s.charAt(i));
            } else if((lastChar == '_' || lastChar == '-') && Character.isAlphabetic(s.charAt(i))) {
                initials.append(s.charAt(i));
            }
            lastChar = s.charAt(i);
        }
        return initials.toString().toUpperCase();
    }

    private StringUtil() {
    }
}
