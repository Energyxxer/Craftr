package com.energyxxer.craftr.compiler.semantic_analysis;

import com.energyxxer.craftr.compiler.exceptions.CraftrException;
import com.energyxxer.craftr.compiler.exceptions.CraftrParserException;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/25/2017.
 */
public class CraftrFile extends AbstractCraftrComponent {
    private SemanticAnalyzer analyzer;
    private File file;

    public String packagePath = null;
    public ArrayList<CraftrUnit> units = new ArrayList<>();

    public CraftrFile(SemanticAnalyzer analyzer, File file, TokenPattern<?> pattern) throws CraftrException {
        super(pattern);
        this.analyzer = analyzer;
        this.file = file;

        TokenPattern<?> packagePattern = pattern.find("PACKAGE.PACKAGE_PATH");

        String realPackage = FileUtil.stripExtension(FileUtil.getRelativePath(file, analyzer.project.getDirectory()).replace(File.separator,"."));
        realPackage = realPackage.substring(0,realPackage.lastIndexOf('.'));
        String packageStatement = packagePattern.flatten(false);
        if(!realPackage.equals(packageStatement)) {
            throw new CraftrParserException("Package name '" + packageStatement + "' does not correspond to the file path '" + realPackage + "'", packagePattern);
        }
        this.packagePath = packagePattern.flatten(false);
        Console.warn.println(packagePath);

        List<TokenPattern<?>> unitPatterns = pattern.find("UNIT_LIST").searchByName("UNIT");

        for(TokenPattern<?> unit : unitPatterns) {
            this.units.add(new CraftrUnit(this, unit));
        }
    }
}
