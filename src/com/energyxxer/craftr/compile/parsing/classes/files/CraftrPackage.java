package com.energyxxer.craftr.compile.parsing.classes.files;

import com.energyxxer.craftr.compile.parsing.classes.units.CraftrUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 12/5/2016.
 */
public class CraftrPackage {
    private CraftrPackage parent = null;

    private String name = null;

    private ArrayList<CraftrPackage> subpackages = new ArrayList<>();
    private ArrayList<CraftrUnit> units = new ArrayList<>();



    public CraftrPackage(String name) {
        this(null, name);
    }
    public CraftrPackage(CraftrPackage parent, String name) {
        this.parent = parent; this.name = name;
    }

    public void add(CraftrUnit unit) {
        unit.setUnitPackage(this);
        units.add(unit);
    }

    public void add(CraftrPackage subpackage) {
        subpackage.parent = this;
        subpackages.add(subpackage);
    }


    public CraftrPackage getSubPackage(String name) {
        for(CraftrPackage subpackage : subpackages) {
            if(subpackage.name.equals(name)) return subpackage;
        }
        return null;
    }


    public boolean contains(String name) {
        for(CraftrPackage subpackage : subpackages) {
            if(subpackage.name.equals(name)) return true;
        }
        return false;
    }


    public String getName() {
        return name;
    }

    public CraftrPackage getParent() {
        return parent;
    }

    public ArrayList<CraftrPackage> getSubpackages() {
        return subpackages;
    }

    public List<CraftrUnit> getUnits() {
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
