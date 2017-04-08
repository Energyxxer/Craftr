package com.energyxxer.craftr.compiler.semantic_analysis;

import com.energyxxer.craftr.compiler.exceptions.CraftrException;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftr.compiler.semantic_analysis.data_types.CraftrTypeRegistry;
import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.logic.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 3/3/2017.
 */
public class SemanticAnalyzer {

    public final Project project;
    public final CraftrTypeRegistry typeRegistry;
    public final ArrayList<CraftrFile> files;

    public SemanticAnalyzer(HashMap<File, TokenPattern<?>> filePatterns, Project project) {

        this.project = project;
        this.typeRegistry = new CraftrTypeRegistry();
        this.files = new ArrayList<>();

        for(File f : filePatterns.keySet()) {
            try {
                files.add(new CraftrFile(this, f, filePatterns.get(f)));
            } catch(CraftrException e) {
                Console.err.println(e.getMessage());
                return;
            }
        }
    }
}
