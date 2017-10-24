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
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.util.FileUtil;
import com.energyxxer.util.vprimitives.VInteger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/25/2017.
 */
public class CraftrFile extends AbstractFileComponent implements Context {

    private SemanticAnalyzer analyzer;
    private File file;

    private final Package parentPackage;
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
            realPackage = null;
        }

        TokenPattern<?> packagePattern = pattern.find("PACKAGE");
        if(packagePattern == null) {
            if(realPackage != null) {
                analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Missing package statement", pattern.getFormattedPath()));
            }
            parentPackage = analyzer.getPackageManager().getRoot();
        } else {
            TokenPattern<?> packagePathPattern = packagePattern.find("PACKAGE_PATH");

            String packageStatement = packagePathPattern.flatten(false);
            if(realPackage == null) {
                analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Package name '" + packageStatement + "' does not correspond to the file path ''", packagePathPattern.getFormattedPath()));
            } else if(!realPackage.equals(packageStatement)) {
                analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Package name '" + packageStatement + "' does not correspond to the file path '" + realPackage + "'", packagePathPattern.getFormattedPath()));
            }
            this.parentPackage = analyzer.getPackageManager().createPackage(packagePathPattern.flatten(false));
        }

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

        for(Symbol sym : analyzer.getLangPackage().getSubSymbolTable()) {
            if(sym instanceof Unit) {
                this.importTable.put(sym);
            } else if(sym instanceof Package) {
                for(Symbol sym2 : ((Package) sym).getSubSymbolTable()) {
                    if(sym2 instanceof Unit) {
                        this.importTable.put(sym2);
                    }
                }
            }
        }

        for(Symbol sym : parentPackage.getSubSymbolTable()) {
            if(sym instanceof Unit) {
                this.importTable.put(sym);
            }
        }

        TokenList importList = (TokenList) pattern.find("IMPORT_LIST");

        if(importList != null) {

            TokenPattern<?>[] imports = importList.getContents();

            //analyzer.getCompiler().getReport().addNotice(new Notice("Debug", NoticeType.INFO, "Imports for file '" + file.getName() + "':"));
            for(TokenPattern<?> rawImport : imports) {
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

    public void resetUnitIDs() {
        units.forEach(Unit::resetUnitID);
    }

    public void assignUnitIDs(VInteger id) {
        units.forEach(u -> u.assignUnitID(id));
    }

    public void checkActionCompatibility() {
        units.forEach(Unit::checkActionCompatibility);
    }

    public void initComponents() {
        units.forEach(Unit::initUnitComponents);
    }

    public void initCodeBlocks() {
        units.forEach(Unit::initCodeBlocks);
    }

    @Override
    public SymbolTable getReferenceTable() {
        return referenceTable;
    }

    public File getIOFile() {
        return file;
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

    @Override
    public Context getParent() {
        return null;
    }

    @Override
    public DataHolder getDataHolder() {
        return null;
    }
}
