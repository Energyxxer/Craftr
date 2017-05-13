package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.compiler.exceptions.CraftrException;
import com.energyxxer.craftrlang.compiler.exceptions.ParserException;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
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

    public CraftrFile(SemanticAnalyzer analyzer, File file, TokenPattern<?> pattern) throws CraftrException {
        super(pattern);
        this.analyzer = analyzer;
        this.file = file;

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
}
