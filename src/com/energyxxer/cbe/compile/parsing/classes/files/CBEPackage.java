package com.energyxxer.cbe.compile.parsing.classes.files;

import com.energyxxer.cbe.compile.parsing.classes.units.CBEUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 12/5/2016.
 */
public class CBEPackage {
    private CBEPackage parent = null;

    private String name = null;

    private ArrayList<CBEPackage> subpackages = new ArrayList<>();
    private ArrayList<CBEUnit> units = new ArrayList<>();



    public CBEPackage(String name) {
        this(null, name);
    }
    public CBEPackage(CBEPackage parent, String name) {
        this.parent = parent; this.name = name;
    }

    public void add(CBEUnit unit) {
        unit.setUnitPackage(this);
        units.add(unit);
    }

    public void add(CBEPackage subpackage) {
        subpackage.parent = this;
        subpackages.add(subpackage);
    }


    public CBEPackage getSubPackage(String name) {
        for(CBEPackage subpackage : subpackages) {
            if(subpackage.name.equals(name)) return subpackage;
        }
        return null;
    }


    public boolean contains(String name) {
        for(CBEPackage subpackage : subpackages) {
            if(subpackage.name.equals(name)) return true;
        }
        return false;
    }


    public String getName() {
        return name;
    }

    public CBEPackage getParent() {
        return parent;
    }

    public ArrayList<CBEPackage> getSubpackages() {
        return subpackages;
    }

    public List<CBEUnit> getUnits() {
        return units;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(parent != null) {
            sb.append(parent.toString());
            sb.append('.');
        }
        sb.append(name);
        return sb.toString();
    }
}
