package com.energyxxer.craftrlang.compiler.semantic_analysis.data_types;

import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.BooleanValue;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.FloatValue;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.IntegerValue;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectivePointer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

import java.util.Arrays;
import java.util.List;

import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.ADD;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.AND;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.DIVIDE;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.EQUAL;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.GREATER_THAN;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.GREATER_THAN_OR_EQUAL;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.LESS_THAN;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.LESS_THAN_OR_EQUAL;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.MODULO;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.MULTIPLY;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.OR;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.SUBTRACT;

/**
 * Created by User on 3/1/2017.
 */
public class DataType {

    private final String name;
    private final boolean primitive;
    private final Unit unit;
    private final boolean nullType;

    private ReferenceConstructor referenceConstructor;
    private TypeOperationPromise typeOperationPromise;

    public DataType(String name) {
        this(name, false);
    }

    public DataType(String name, boolean primitive) {
        this.name = name;
        this.primitive = primitive;
        this.unit = null;
        this.nullType = false;
    }

    public DataType(Unit unit) {
        this.name = unit.getFullyQualifiedName();
        this.primitive = false;
        this.unit = unit;
        this.nullType = false;
    }

    //Null data type
    public DataType() {
        this.name = "null";
        this.primitive = true;
        this.unit = null;
        this.nullType = true;
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

    public static final DataType NULL = new DataType();

    public static final DataType OBJECT = new DataType("craftr.lang.Object", false);
    public static final DataType STRING = new DataType("craftr.lang.String", false);
    public static final DataType TEMP_ARRAY = new DataType("TempArray", false);

    public static final DataType[] PRIMITIVES = new DataType[] {VOID, INT, FLOAT, DOUBLE, CHAR, LONG, BOOLEAN};
    public static final DataType[] DEFAULT_TYPES = new DataType[] {OBJECT, STRING};

    static {
        INT.setReferenceConstructor(IntegerValue::new);
        FLOAT.setReferenceConstructor(FloatValue::new);
        //INT.setReferenceConstructor(IntegerValue::new);
        BOOLEAN.setReferenceConstructor(BooleanValue::new);
        //OBJECT.setReferenceConstructor({});

        INT.setTypeOperationPromise((op, other)->{
            if(Arrays.asList(INT, FLOAT, DOUBLE, LONG).contains(other)) {
                if(Arrays.asList(ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO).contains(op)) return other;
                else if(Arrays.asList(EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL).contains(op)) return BOOLEAN;
            } else if(other == STRING) {
                if(op == ADD) return other;
            }
            return null;
        });
        FLOAT.setTypeOperationPromise((op, other)->{
            if(Arrays.asList(INT, FLOAT, DOUBLE, LONG).contains(other)) {
                if(Arrays.asList(ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO).contains(op)) return ((other == INT) ? FLOAT : other);
                else if(Arrays.asList(EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL).contains(op)) return BOOLEAN;
            } else if(other == STRING) {
                if(op == ADD) return other;
            }
            return null;
        });
        BOOLEAN.setTypeOperationPromise((op, other)->{
            if(other == BOOLEAN) {
                if(Arrays.asList(AND, OR, EQUAL).contains(op)) return BOOLEAN;
            } else if(other == STRING) {
                if(op == ADD) return STRING;
            }
            return null;
        });
    }

    public static DataType parseType(List<Token> flatTokens, SymbolTable table, Context context) {
        if(flatTokens.size() == 1) {
            for(DataType type : PRIMITIVES) {
                if(type.getName().equals(flatTokens.get(0).value)) return type;
            }
            for(DataType type : DEFAULT_TYPES) {
                if(type.getName().equals(flatTokens.get(0).value)) return type;
            }
        }
        if(flatTokens.size() >= 3 && flatTokens.get(1).type == CraftrLang.BRACE) {
            return TEMP_ARRAY;
        }
        Symbol symbol = table.getSymbol(flatTokens, context);
        if(symbol == null) {
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "Symbol not found:" + flatTokens + " at context" + context, flatTokens.get(0).getFormattedPath()));
            return OBJECT;
        }
        if(!(symbol instanceof Unit)) {
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "'" + flatTokens.get(flatTokens.size()-1).value + "' is not a unit",flatTokens.get(flatTokens.size()-1).getFormattedPath()));
            return OBJECT;
        }

        return ((Unit) symbol).getDataType();
    }

    public DataType getReturnType(Operator op, DataType other) {
        return (typeOperationPromise != null) ? typeOperationPromise.check(op, other) : null;
    }

    public void setTypeOperationPromise(TypeOperationPromise typeOperationPromise) {
        this.typeOperationPromise = typeOperationPromise;
    }

    public Value createImplicit(ObjectivePointer reference, Context context) {
        if(referenceConstructor != null) return referenceConstructor.create(reference, context);
        else return null;
    }

    public void setReferenceConstructor(ReferenceConstructor referenceConstructor) {
        this.referenceConstructor = referenceConstructor;
    }

    public SymbolTable getSubSymbolTable() {
        //DO SOMETHING ABOUT THE FIELD TABLE THING
        return (unit != null) ? unit.getStaticFieldLog() : null;
    }

    public MethodLog getMethodLog() {
        return (unit != null) ? unit.getInstanceMethodLog() : null;
    }

    public boolean isNullType() {
        return nullType;
    }

    public boolean instanceOf(DataType type) {
        if(this.isNullType()) return true; //Yeah yeah null can be anything it wants to be, now get lost.
        if(type.isNullType()) return false; //Woah woah there buddy, null is unique; nobody else can be null.

        if(this.primitive || type.primitive) return this.equals(type);
        if(this.unit != null && type.unit != null)
        return this.unit.instanceOf(type.unit);
        else return this.name.equals(type.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataType dataType = (DataType) o;

        return name.equals(dataType.name);
    }

    @Override
    public String toString() {
        return name;
    }
}