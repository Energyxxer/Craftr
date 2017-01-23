package com.energyxxer.cbe.compile.parsing.classes.files;

import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.compile.exceptions.CBEParserException;

/**
 * Created by User on 12/7/2016.
 */
public class CBEPackageManager {
    private CBEPackage root;

    public CBEPackageManager(CBEPackage root) {
        this.root = root;
    }

    public CBEPackage create(String path, TokenPattern<?> packagePattern) throws CBEParserException {
        if(!(path + ".").startsWith(root.getName() + '.')) throw new CBEParserException(String.format("%s cannot be a subpackage of %s!",path,root.toString()), packagePattern);

        String[] packageNames = path.split("\\.");
        CBEPackage currentPackage = root;

        boolean firstPackage = true;
        for(String name : packageNames) {
            if(firstPackage) {
                firstPackage = false;
                continue;
            }
            if(!currentPackage.contains(name)) {
                CBEPackage subpackage = new CBEPackage(name);
                currentPackage.add(subpackage);
                currentPackage = subpackage;
            } else currentPackage = currentPackage.getSubPackage(name);
        }

        return currentPackage;
    }
}
