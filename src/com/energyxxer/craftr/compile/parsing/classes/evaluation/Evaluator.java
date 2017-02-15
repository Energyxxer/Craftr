package com.energyxxer.craftr.compile.parsing.classes.evaluation;

import com.energyxxer.craftr.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.craftr.compile.parsing.classes.values.CraftrValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 1/30/2017.
 */
public class Evaluator {

    private static HashMap<String, ArrayList<PatternParser>> evaluators = new HashMap<>();

    public static void addEvaluator(String name, PatternParser p) {
        if(!evaluators.containsKey(name)) {
            evaluators.put(name,new ArrayList<>());
        }
        evaluators.get(name).add(p);
    }

    public static CraftrValue eval(TokenPattern<?> p) {
        for(String key : evaluators.keySet()) {
            if(p.name.equals(key)) {
                ArrayList<PatternParser> entries = evaluators.get(key);
                for(PatternParser entry : entries) {
                    CraftrValue val = entry.eval(p);
                    if(val != null) {
                        return val;
                    }
                }
                return null;
            }
        }
        return null;
    }
}