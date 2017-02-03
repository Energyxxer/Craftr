package com.energyxxer.craftr.compile.parsing.classes.files;

import com.energyxxer.craftr.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.craftr.compile.exceptions.CraftrParserException;

/**
 * Created by User on 12/7/2016.
 */
public class CraftrPackageManager {
    private CraftrPackage root;

    public CraftrPackageManager(CraftrPackage root) {
        this.root = root;
    }

    public CraftrPackage create(String path, TokenPattern<?> packagePattern) throws CraftrParserException {
        if(!(path + ".").startsWith(root.getName() + '.')) throw new CraftrParserException(String.format("%s cannot be a subpackage of %s!",path,root.toString()), packagePattern);

        String[] packageNames = path.split("\\.");
        CraftrPackage currentPackage = root;

        boolean firstPackage = true;
        for(String name : packageNames) {
            if(firstPackage) {
                firstPackage = false;
                continue;
            }
            if(!currentPackage.contains(name)) {
                CraftrPackage subpackage = new CraftrPackage(name);
                currentPackage.add(subpackage);
                currentPackage = subpackage;
            } else currentPackage = currentPackage.getSubPackage(name);
        }

        return currentPackage;
    }
}
