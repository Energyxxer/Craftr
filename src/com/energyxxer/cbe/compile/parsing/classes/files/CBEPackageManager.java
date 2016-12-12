package com.energyxxer.cbe.compile.parsing.classes.files;

/**
 * Created by User on 12/7/2016.
 */
public class CBEPackageManager {
    private CBEPackage root;

    public CBEPackageManager(CBEPackage root) {
        this.root = root;
    }

    public CBEPackage create(String path) {
        if(!path.startsWith(root.getName() + '.')) throw new IllegalArgumentException(String.format("%s cannot be a subpackage of %s!",path,root.toString()));

        String[] packageNames = path.split("\\.");
        CBEPackage currentPackage = root;

        for(String name : packageNames) {
            if(!currentPackage.contains(name)) {
                CBEPackage subpackage = new CBEPackage(name);
                currentPackage.add(subpackage);
                currentPackage = subpackage;
            } else currentPackage = currentPackage.getSubPackage(name);
        }

        return currentPackage;
    }
}
