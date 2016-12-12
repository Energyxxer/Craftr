package com.energyxxer.cbe.compile.parsing.classes.registries;

import com.energyxxer.cbe.compile.parsing.classes.units.CBEUnit;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by User on 12/4/2016.
 */
public class UnitRegistry implements Iterable<CBEUnit> {

    private HashMap<String, CBEUnit> units = new HashMap<>();

    public void add(CBEUnit u) {
        units.put(u.getName(),u);
    }

    @Override
    public Iterator<CBEUnit> iterator() {
        return units.values().iterator();
    }
}
