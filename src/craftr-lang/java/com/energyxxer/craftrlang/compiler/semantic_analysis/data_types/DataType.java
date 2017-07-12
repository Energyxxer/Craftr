package com.energyxxer.craftrlang.compiler.semantic_analysis.data_types;

import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;

/**
 * Created by User on 3/1/2017.
 */
public class DataType {

    private final String name;
    private final boolean primitive;

    public DataType(String name) {
        this(name, false);
    }

    public DataType(String name, boolean primitive) {
        this.name = name;
        this.primitive = primitive;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    //Default types;
    public static final DataType INT = new DataType("int", true);
    public static final DataType FLOAT = new DataType("float", true);
    public static final DataType CHAR = new DataType("char", true);
    public static final DataType BOOLEAN = new DataType("boolean", true);

    public static final DataType STRING = new DataType("String", false);

    public static final DataType[] PRIMITIVES = new DataType[] {INT, FLOAT, CHAR, BOOLEAN};
    public static final DataType[] DEFAULT_TYPES = new DataType[] {STRING};

    public static DataType parseType(String str, SymbolTable table) {
        for(DataType type : PRIMITIVES) {
            if(type.getName().equals(str)) return type;
        }
        for(DataType type : DEFAULT_TYPES) {
            if(type.getName().equals(str)) return type;
        }
        return new DataType(str);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof DataType) && this.name.equals(((DataType) o).name);
    }

    @Override
    public String toString() {
        return name;
    }
}