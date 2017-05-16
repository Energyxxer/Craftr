package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.compiler.exceptions.CompilerException;
import com.energyxxer.craftrlang.compiler.exceptions.CraftrException;
import com.energyxxer.craftrlang.compiler.exceptions.ParserException;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.util.FileUtil;
import com.energyxxer.util.out.Console;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/25/2017.
 */
public class CraftrFile extends AbstractFileComponent {
    private SemanticAnalyzer analyzer;
    private File file;

    private Package parentPackage = null;
    public ArrayList<Unit> units = new ArrayList<>();

    public SymbolTable importTable;
    private boolean importsInitialized = false;

    private Context context;

    public CraftrFile(SemanticAnalyzer analyzer, File file, TokenPattern<?> pattern) throws CraftrException {
        super(pattern);
        this.analyzer = analyzer;
        this.file = file;
        this.context = new Context(this);
        this.importTable = new SymbolTable(analyzer.getCompiler());

        TokenPattern<?> packagePattern = pattern.find("PACKAGE.PACKAGE_PATH");

        String realPackage = FileUtil.stripExtension(FileUtil.getRelativePath(file, analyzer.rootPath).replace(File.separator,"."));
        realPackage = realPackage.substring(0,realPackage.lastIndexOf('.'));
        String packageStatement = packagePattern.flatten(false);
        if(!realPackage.equals(packageStatement)) {
            throw new ParserException("Package name '" + packageStatement + "' does not correspond to the file path '" + realPackage + "'", packagePattern);
        }
        this.parentPackage = analyzer.getPackageManager().createPackage(packagePattern.flatten(false));
        Console.warn.println(parentPackage.getFullyQualifiedName());

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

    public void initImports() throws CraftrException {
        if(importsInitialized) return;

        TokenList importList = (TokenList) pattern.find("IMPORT_LIST");
        if(importList == null) {
            importsInitialized = true;
            analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.INFO, "No imports found for file '" + file.getName() + "'"));
            return;
        }

        TokenPattern<?>[] imports = importList.getContents();

        Console.info.println("Imports for file '" + file.getName() + "':");
        for (TokenPattern<?> rawImport : imports) {
            TokenPattern<?> identifier = rawImport.find("IDENTIFIER");
            String fullyQualifiedName = identifier.flatten(false);
            String shortName = fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf('.')+1);

            Symbol itemToImport;
            try {
                itemToImport = analyzer.getSymbolTable().getSymbol(fullyQualifiedName, context);
            } catch(CompilerException x) {
                analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, x.getMessage(), identifier.getFormattedPath()));
                continue;
            }
            if(!(itemToImport instanceof Unit)) {
                analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Invalid import: '" + itemToImport.getName() + "' isn't an unit", identifier.getFormattedPath()));
                continue;
            }
            this.importTable.put(shortName, itemToImport);

            {
                Notice n = new Notice(NoticeType.INFO, "Saving '" + fullyQualifiedName + "' as '" + shortName + "'", "\b" + file.getPath() + "\b0\b0\b" + file.getName() + ":1:1#0");
                n.setLabel("\bImports for file '" + file.getName() + "':");
                analyzer.getCompiler().getReport().addNotice(n);
            }
        }
        importsInitialized = true;
    }

    public SemanticAnalyzer getAnalyzer() {
        return analyzer;
    }
}
