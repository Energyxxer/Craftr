package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.ContextType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/25/2017.
 */
public class CraftrFile extends AbstractFileComponent implements Context {

    private SemanticAnalyzer analyzer;
    private File file;

    private Package parentPackage = null;
    private ArrayList<Unit> units = new ArrayList<>();

    private SymbolTable importTable;
    private SymbolTable referenceTable;

    private boolean importsInitialized = false;

    public CraftrFile(SemanticAnalyzer analyzer, File file, TokenPattern<?> pattern) {
        super(pattern);
        this.analyzer = analyzer;
        this.file = file;
        this.importTable = new SymbolTable(analyzer.getCompiler());

        String realPackage = FileUtil.stripExtension(FileUtil.getRelativePath(file, analyzer.sourcePath).replace(File.separator,"."));
        if(realPackage.contains(".")) {
            realPackage = realPackage.substring(0,realPackage.lastIndexOf('.'));
        } else {
            return;
        }

        TokenPattern<?> packagePattern = pattern.find("PACKAGE");
        if(packagePattern == null) {
            analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Missing package statement", pattern.getFormattedPath()));
            return;
        } else {
            TokenPattern<?> packagePathPattern = packagePattern.find("PACKAGE_PATH");

            String packageStatement = packagePathPattern.flatten(false);
            if(!realPackage.equals(packageStatement)) {
                analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Package name '" + packageStatement + "' does not correspond to the file path '" + realPackage + "'", packagePathPattern.getFormattedPath()));
            }
            this.parentPackage = analyzer.getPackageManager().createPackage(packagePathPattern.flatten(false));
        }

        //analyzer.getCompiler().getReport().addNotice(new Notice("Debug", NoticeType.WARNING, parentPackage.getFullyQualifiedName()));

        List<TokenPattern<?>> unitPatterns = pattern.find("UNIT_LIST").searchByName("UNIT");

        for(TokenPattern<?> rawUnit : unitPatterns) {
            Unit unit = new Unit(this, rawUnit);
            this.units.add(unit);
            this.parentPackage.addUnit(unit);
        }
    }

    public Package getPackage() {
        return parentPackage;
    }

    public void initImports() {
        if(importsInitialized) return;

        for(Symbol sym : parentPackage.getSubSymbolTable()) {
            if(sym instanceof Unit) {
                this.importTable.put(sym);
            }
        }

        TokenList importList = (TokenList) pattern.find("IMPORT_LIST");
        if(importList == null) {
            importsInitialized = true;

            for(Symbol sym : parentPackage.getSubSymbolTable()) {
                if(sym instanceof Unit) {
                    this.importTable.put(sym);
                }
            }
            this.referenceTable = analyzer.getSymbolTable().mergeWith(importTable);
            //analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.INFO, "No imports found for file '" + file.getName() + "'"));
            return;
        }

        TokenPattern<?>[] imports = importList.getContents();

        //analyzer.getCompiler().getReport().addNotice(new Notice("Debug", NoticeType.INFO, "Imports for file '" + file.getName() + "':"));
        for (TokenPattern<?> rawImport : imports) {
            TokenPattern<?> identifier = rawImport.find("IMPORT_IDENTIFIER");
            List<Token> flatTokens = identifier.flattenTokens();

            boolean wildcard = flatTokens.get(flatTokens.size()-1).value.equals("*");
            if(wildcard) flatTokens = flatTokens.subList(0, flatTokens.size()-2);

            Symbol itemToImport;
            itemToImport = analyzer.getSymbolTable().getSymbol(flatTokens, this);
            if(itemToImport != null) {
                if(wildcard) {
                    if(itemToImport.getSubSymbolTable() == null) {
                        analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Invalid import: '" + itemToImport.getName() + "' is not a data structure", identifier.getFormattedPath()));
                    } else {
                        for(Symbol symbol : itemToImport.getSubSymbolTable()) {
                            this.importTable.put(symbol);
                        }
                    }
                } else {
                    if(!(itemToImport instanceof Unit)) {
                        analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Invalid import: '" + itemToImport.getName() + "' isn't a unit", identifier.getFormattedPath()));
                        continue;
                    }
                    this.importTable.put(itemToImport);
                }
            }
        }
        importsInitialized = true;

        this.referenceTable = analyzer.getSymbolTable().mergeWith(importTable);
    }

    public void initActions() {
        units.forEach(Unit::initUnitActions);
    }

    public void buildInheritanceMap() {
        units.forEach(Unit::buildInheritanceMap);
    }

    public void checkActionCompatibility() {
        units.forEach(Unit::checkActionCompatibility);
    }

    public void initComponents() {
        units.forEach(Unit::initUnitComponents);
    }

    public SymbolTable getImportTable() {
        return importTable;
    }

    public SymbolTable getReferenceTable() {
        return referenceTable;
    }

    @Override
    public CraftrFile getDeclaringFile() {
        return this;
    }

    @Override
    public Unit getUnit() {
        return null;
    }

    @Override
    public ContextType getContextType() {
        return ContextType.FILE;
    }

    @Override
    public SemanticAnalyzer getAnalyzer() {
        return analyzer;
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
