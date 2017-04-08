package com.energyxxer.craftr.compiler.semantic_analysis.data_types;

import java.util.HashMap;

/**
 * Created by User on 3/1/2017.
 */
public class CraftrTypeRegistry {

    public HashMap<String, CraftrDataType> types = new HashMap<>();

    public CraftrTypeRegistry() {
        populate();
    }

    public void add(CraftrDataType type) {
        types.put(type.getName(), type);
    }

    public CraftrDataType get(String key) {
        return types.get(key);
    }

    public HashMap<String, CraftrDataType> getTypes() {
        return types;
    }

    private void populate() {
        for(CraftrDataType primitive : CraftrDataType.PRIMITIVES) {
            add(primitive);
        }
    }


}
