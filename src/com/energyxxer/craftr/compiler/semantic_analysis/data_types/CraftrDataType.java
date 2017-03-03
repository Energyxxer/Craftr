package com.energyxxer.craftr.compiler.semantic_analysis.data_types;

/**
 * Created by User on 3/1/2017.
 */
public class CraftrDataType {

    private final String name;
    private final boolean primitive;

    public CraftrDataType(String name) {
        this(name, false);
    }

    public CraftrDataType(String name, boolean primitive) {
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
    private static final CraftrDataType INT = new CraftrDataType("int", true);
    private static final CraftrDataType FLOAT = new CraftrDataType("float", true);
    private static final CraftrDataType STRING = new CraftrDataType("String", true);
    private static final CraftrDataType CHAR = new CraftrDataType("char", true);
    private static final CraftrDataType BOOLEAN = new CraftrDataType("boolean", true);

    public static final CraftrDataType[] PRIMITIVES = new CraftrDataType[] {INT, FLOAT, STRING, CHAR, BOOLEAN};
}