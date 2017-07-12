package com.energyxxer.craftrlang.compiler.semantic_analysis.data_types;

import java.util.HashMap;

/**
 * Created by User on 3/1/2017.
 */
public class TypeRegistry {

    public HashMap<String, DataType> types = new HashMap<>();

    public TypeRegistry() {
        populate();
    }

    public void add(DataType type) {
        types.put(type.getName(), type);
    }

    public DataType get(String key) {
        return types.get(key);
    }

    public HashMap<String, DataType> getTypes() {
        return types;
    }

    private void populate() {
        for(DataType primitive : DataType.PRIMITIVES) {
            add(primitive);
        }
        for(DataType defType : DataType.DEFAULT_TYPES) {
            add(defType);
        }
    }


}
