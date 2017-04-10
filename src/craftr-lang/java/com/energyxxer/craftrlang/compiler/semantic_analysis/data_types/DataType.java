package com.energyxxer.craftrlang.compiler.semantic_analysis.data_types;

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
    private static final DataType INT = new DataType("int", true);
    private static final DataType FLOAT = new DataType("float", true);
    private static final DataType STRING = new DataType("String", true);
    private static final DataType CHAR = new DataType("char", true);
    private static final DataType BOOLEAN = new DataType("boolean", true);

    public static final DataType[] PRIMITIVES = new DataType[] {INT, FLOAT, STRING, CHAR, BOOLEAN};

    @Override
    public boolean equals(Object o) {
        return (o instanceof DataType) && this.name.equals(((DataType) o).name);
    }
}