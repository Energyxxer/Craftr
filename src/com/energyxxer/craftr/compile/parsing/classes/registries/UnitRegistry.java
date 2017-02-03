package com.energyxxer.craftr.compile.parsing.classes.registries;

import com.energyxxer.craftr.compile.parsing.classes.units.CraftrUnit;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by User on 12/4/2016.
 */
public class UnitRegistry implements Iterable<CraftrUnit> {

    private HashMap<String, CraftrUnit> units = new HashMap<>();

    public void add(CraftrUnit u) {
        units.put(u.getName(),u);
    }

    @Override
    public Iterator<CraftrUnit> iterator() {
        return units.values().iterator();
    }
}
