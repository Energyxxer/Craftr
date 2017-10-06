package com.energyxxer.craftrlang.compiler.semantic_analysis.data_types;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenType;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;

import java.util.List;

/**
 * Created by User on 3/1/2017.
 */
public class DataType {

    private final String name;
    private final boolean primitive;
    private final Unit unit;

    public DataType(String name) {
        this(name, false);
    }

    public DataType(String name, boolean primitive) {
        this.name = name;
        this.primitive = primitive;
        this.unit = null;
    }

    public DataType(Unit unit) {
        this.name = unit.getFullyQualifiedName();
        this.primitive = false;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    //Default types;
    public static final DataType VOID = new DataType("void", true);
    public static final DataType INT = new DataType("int", true);
    public static final DataType FLOAT = new DataType("float", true);
    public static final DataType DOUBLE = new DataType("double", true);
    public static final DataType CHAR = new DataType("char", true);
    public static final DataType LONG = new DataType("long", true);
    public static final DataType BOOLEAN = new DataType("boolean", true);

    public static final DataType OBJECT = new DataType("craftr.lang.Object", false);
    public static final DataType STRING = new DataType("craftr.lang.String", false);
    public static final DataType TEMP_ARRAY = new DataType("TempArray", false);

    public static final DataType[] PRIMITIVES = new DataType[] {VOID, INT, FLOAT, DOUBLE, CHAR, LONG, BOOLEAN};
    public static final DataType[] DEFAULT_TYPES = new DataType[] {OBJECT, STRING};

    public static DataType parseType(List<Token> flatTokens, SymbolTable table, Context context) {
        if(flatTokens.size() == 1) {
            for(DataType type : PRIMITIVES) {
                if(type.getName().equals(flatTokens.get(0).value)) return type;
            }
            for(DataType type : DEFAULT_TYPES) {
                if(type.getName().equals(flatTokens.get(0).value)) return type;
            }
        }
        if(flatTokens.size() >= 3 && flatTokens.get(1).type == TokenType.BRACE) {
            return TEMP_ARRAY;
        }
        Symbol symbol = table.getSymbol(flatTokens, context);
        if(symbol == null) return OBJECT;
        if(!(symbol instanceof Unit)) {
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "'" + flatTokens.get(flatTokens.size()-1).value + "' is not a unit",flatTokens.get(flatTokens.size()-1).getFormattedPath()));
            return OBJECT;
        }

        return new DataType((Unit) symbol);
    }

    public SymbolTable getSubSymbolTable() {
        //DO SOMETHING ABOUT THE FIELD TABLE THING
        return (unit != null) ? unit.getStaticFieldLog().getFieldTable() : null;
    }

    public MethodLog getMethodLog() {
        return (unit != null) ? unit.getInstanceMethodLog() : null;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof DataType) && this.name.equals(((DataType) o).name);
    }

    public boolean instanceOf(DataType type) {
        if(this.primitive || type.primitive) return this.equals(type);
        if(this.unit != null && type.unit != null)
        return this.unit.instanceOf(type.unit);
        else return this.name.equals(type.name);
    }

    @Override
    public String toString() {
        return name;
    }
}