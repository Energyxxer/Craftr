package com.energyxxer.craftr.compile.parsing.classes.files;

import com.energyxxer.craftr.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.craftr.compile.exceptions.CraftrParserException;
import com.energyxxer.craftr.compile.parsing.Parser;
import com.energyxxer.craftr.compile.parsing.classes.units.CraftrUnit;
import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.global.ProjectManager;
import com.energyxxer.craftr.logic.Project;
import com.energyxxer.craftr.util.FileUtil;
import com.energyxxer.craftr.util.StringUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by User on 12/2/2016.
 */
public class CraftrFile {
    private final String name;
    private final CraftrPackage filePackage;

    private final ArrayList<String> imports = new ArrayList<>();
    private final ArrayList<CraftrUnit> units = new ArrayList<>();

    private final Project project;

    private final File file;
    private final TokenPattern pattern;

    public CraftrFile(Parser parser, File file, TokenPattern<?> pattern) throws CraftrParserException {

        this.name = StringUtil.stripExtension(file.getName());
        this.project = ProjectManager.getAssociatedProject(file);
        if(project == null) throw new CraftrParserException("What the heck? A file not in the project?\n\tat file " + file);
        this.file = file;

        TokenPattern<?> packagePattern = pattern.deepSearchByName("PACKAGE_PATH").get(0);

        String realPackage = StringUtil.stripExtension(FileUtil.getRelativePath(file, project.getDirectory()).replace(File.separator,"."));
        realPackage = realPackage.substring(0,realPackage.lastIndexOf('.'));
        String packageStatement = packagePattern.flatten(false);
        if(!realPackage.equals(packageStatement)) {
            throw new CraftrParserException("Package name '" + packageStatement + "' does not correspond to the file path '" + realPackage + "'", packagePattern);
        }

        this.filePackage = parser.packageManager.create(realPackage, packagePattern);

        this.pattern = pattern;

        Console.debug.println(filePackage + " : " + packageStatement + "... at file " + name);
    }

    public String getName() {
        return name;
    }

    public CraftrPackage getPackage() {
        return filePackage;
    }

    public ArrayList<CraftrUnit> getUnits() {
        return units;
    }

    public ArrayList<String> getImports() {
        return imports;
    }

    public Project getProject() {
        return project;
    }

    public File getFile() {
        return file;
    }
}
