package com.energyxxer.cbe.compile.parsing.classes.files;

import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.compile.parsing.Parser;
import com.energyxxer.cbe.compile.parsing.classes.units.CBEUnit;
import com.energyxxer.cbe.compile.parsing.exceptions.CBEParserException;
import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.logic.Project;
import com.energyxxer.cbe.util.FileUtil;
import com.energyxxer.cbe.util.StringUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by User on 12/2/2016.
 */
public class CBEFile {
    private final String name;
    private final CBEPackage filePackage;

    private final ArrayList<String> imports = new ArrayList<>();
    private final ArrayList<CBEUnit> units = new ArrayList<>();

    private final Project project;

    private final File file;
    private final TokenPattern pattern;

    public CBEFile(Parser parser, File file, TokenPattern<?> pattern) throws CBEParserException {

        this.name = StringUtil.stripExtension(file.getName());
        this.project = ProjectManager.getAssociatedProject(file);
        this.file = file;

        TokenPattern<?> packagePattern = pattern.searchByName("PACKAGE_PATH").get(0);

        String realPackage = StringUtil.stripExtension(FileUtil.getRelativePath(file, project.directory).replace(File.separator,"."));
        realPackage = realPackage.substring(0,realPackage.lastIndexOf('.'));
        String packageStatement = packagePattern.flatten(false);
        if(!realPackage.equals(packageStatement)) {
            throw new CBEParserException("Package name '" + packageStatement + "' does not correspond to the file path '" + realPackage + "'", packagePattern);
        }

        this.filePackage = parser.packageManager.create(realPackage);

        this.pattern = pattern;

        System.out.println(filePackage + " : " + packageStatement + "... at file " + name);
    }

    public String getName() {
        return name;
    }

    public CBEPackage getPackage() {
        return filePackage;
    }

    public ArrayList<CBEUnit> getUnits() {
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
