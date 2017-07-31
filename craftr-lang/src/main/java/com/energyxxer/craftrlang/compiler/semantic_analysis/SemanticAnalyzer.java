package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.PackageManager;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 3/3/2017.
 */
public class SemanticAnalyzer {
    private final Compiler compiler;

    public final File sourcePath;
    public final ArrayList<CraftrFile> files;

    private final SymbolTable symbolTable;

    private final PackageManager packageManager;

    private final HashMap<File, TokenPattern<?>> filePatterns;

    public SemanticAnalyzer(Compiler compiler, HashMap<File, TokenPattern<?>> filePatterns, File sourcePath) {
        this.compiler = compiler;
        this.sourcePath = sourcePath;
        this.files = new ArrayList<>();
        this.symbolTable = new SymbolTable(compiler);
        this.packageManager = new PackageManager(this.symbolTable);
        this.filePatterns = filePatterns;
    }

    public void join(SemanticAnalyzer analyzer) {
        this.files.addAll(analyzer.files);
        this.symbolTable.putAll(analyzer.getSymbolTable());
    }

    public void start() {
        for(File f : filePatterns.keySet()) {
            files.add(new CraftrFile(this, f, filePatterns.get(f)));
        }

        files.forEach(CraftrFile::initImports);
        files.forEach(CraftrFile::initActions);
        files.forEach(CraftrFile::buildInheritanceMap);
        files.forEach(CraftrFile::initComponents);
        files.forEach(CraftrFile::checkActionCompatibility);
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public PackageManager getPackageManager() {
        return packageManager;
    }

    public Compiler getCompiler() {
        return compiler;
    }
}
