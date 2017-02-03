package com.energyxxer.craftr.compile.analysis;

import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 1/29/2017.
 */
public class SpecialTokenConstant {
    List<String> patterns;
    String type;

    public SpecialTokenConstant(List<String> patterns, String type) {
        this.patterns = patterns;
        this.type = type;
    }

    public SpecialTokenConstant(String[] patterns, String type) {
        this.patterns = Arrays.asList(patterns);
        this.type = type;
    }
}
