package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.PackageManager;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.util.StringUtil;
import com.energyxxer.util.vprimitives.VInteger;

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

    private Package langPackage = null;

    private int nextID = 0;

    public SemanticAnalyzer(Compiler compiler, HashMap<File, TokenPattern<?>> filePatterns, File sourcePath) {
        this.compiler = compiler;
        this.sourcePath = sourcePath;
        this.files = new ArrayList<>();
        this.symbolTable = new SymbolTable(compiler);
        this.packageManager = new PackageManager(this.symbolTable, this);
        this.filePatterns = filePatterns;
    }

    public void join(SemanticAnalyzer analyzer) {
        this.files.addAll(analyzer.files);
        this.symbolTable.putAll(analyzer.getSymbolTable());
        this.langPackage = analyzer.langPackage;
        //TODO: Join with the package manager, somehow...
        this.nextID = analyzer.nextID;
    }

    public void start() {
        for(File f : filePatterns.keySet()) {
            files.add(new CraftrFile(this, f, filePatterns.get(f)));
        }

        //Stage 1
        files.forEach(CraftrFile::initImports);
        //Stage 2
        files.forEach(CraftrFile::initActions);
        //Stage 3
        files.forEach(CraftrFile::buildInheritanceMap);
        //Stage 4
        VInteger id = new VInteger(0);
        files.forEach(CraftrFile::resetUnitIDs);
        getCompiler().getReport().addNotice(new Notice("Unit IDs", NoticeType.INFO,"IDs Reset"));
        files.forEach(f->f.assignUnitIDs(id));
        this.nextID = id.value;
        //Stage 5
        files.forEach(CraftrFile::initComponents);
        //Stage 6
        files.forEach(CraftrFile::checkActionCompatibility);
        //Stage 7
        files.forEach(CraftrFile::initCodeBlocks);
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

    public Package getLangPackage() {
        return langPackage;
    }

    public void setLangPackage(Package langPackage) {
        this.langPackage = langPackage;
    }
}
